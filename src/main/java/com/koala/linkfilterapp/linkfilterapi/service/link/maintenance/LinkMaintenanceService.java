package com.koala.linkfilterapp.linkfilterapi.service.link.maintenance;

import com.koala.linkfilterapp.linkfilterapi.api.common.entity.RequestHistory;
import com.koala.linkfilterapp.linkfilterapi.api.common.enums.LinkSortType;
import com.koala.linkfilterapp.linkfilterapi.api.common.enums.LinkStatus;
import com.koala.linkfilterapp.linkfilterapi.api.common.enums.ReportType;
import com.koala.linkfilterapp.linkfilterapi.api.common.enums.RequestType;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.LinkException;
import com.koala.linkfilterapp.linkfilterapi.api.link.dto.request.CreateLinkRequest;
import com.koala.linkfilterapp.linkfilterapi.api.link.dto.request.LinkUpdate;
import com.koala.linkfilterapp.linkfilterapi.api.link.dto.request.LinkUpdateRequest;
import com.koala.linkfilterapp.linkfilterapi.api.link.dto.response.LinkBean;
import com.koala.linkfilterapp.linkfilterapi.api.link.entity.Link;
import com.koala.linkfilterapp.linkfilterapi.repository.LinkRepository;
import com.koala.linkfilterapp.linkfilterapi.service.common.impl.RequestHistoryServiceImpl;
import com.koala.linkfilterapp.linkfilterapi.service.link.LinkConverter;
import com.koala.linkfilterapp.linkfilterapi.service.link.impl.LinkConnectionValidationService;
import com.koala.linkfilterapp.linkfilterapi.service.link.impl.LinkValidationServiceImpl;
import com.koala.linkfilterapp.linkfilterapi.service.link.impl.LinkWhoIsService;
import com.koala.linkfilterapp.linkfilterapi.service.report.ReportService;
import org.dozer.DozerBeanMapper;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.koala.linkfilterapp.linkfilterapi.service.common.CommonApiConstants.REPORT_LINK_CD;
import static com.koala.linkfilterapp.linkfilterapi.service.common.CommonApiConstants.SERVICE_LOG_MESSAGE;
import static com.koala.linkfilterapp.linkfilterapi.service.link.LinkConverter.convert;
import static com.koala.linkfilterapp.linkfilterapi.service.link.validator.LinkValidator.parseUrlToDomainString;
import static com.koala.linkfilterapp.linkfilterapi.service.link.validator.LinkValidator.validateReportType;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class LinkMaintenanceService {
    Logger log = Logger.getLogger("LinkMaintenanceService");

    @Autowired
    LinkRepository repository;

    @Autowired
    RequestHistoryServiceImpl requestHistoryService;

    @Autowired
    ReportService reportService;

    @Autowired
    LinkConnectionValidationService connectionValidationService;

    @Autowired
    LinkWhoIsService whoIsService;

    @Autowired
    LinkValidationServiceImpl validationService;

    public Link createLink(CreateLinkRequest request) {
        Link entity = new DozerBeanMapper().map(request, Link.class);
        setLastModifiedInfo(entity);
        whoIsService.setWhoIsDate(entity);
        Link res = repository.save(entity);
        log.info("Saved: " + entity);
        return res;
    }

    public Page<LinkBean> getAllLinks(int pageNum, int size, LinkSortType sortType) {
        Pageable page = PageRequest.of(pageNum, size).withSort(Sort.by(sortType.toString()).ascending());
        Page<Link> found = repository.findAll(page);
        return found.map(LinkConverter::convert);
    }

    public Page<LinkBean> searchLink(int pageNum, int size, LinkSortType sortType, String url) {
        Pageable page = PageRequest.of(pageNum, size).withSort(Sort.by(sortType.toString()).ascending());
        Page<Link> found = repository.findByUrlContains(url, page);
        return found.map(LinkConverter::convert);
    }

    public LinkBean getLink(String url) throws LinkException {
        Optional<Link> res = repository.findById(url);
        if (res.isPresent()) {
            return LinkConverter.convert(res.get());
        } else {
            LinkException exception = new LinkException(HttpStatus.NOT_FOUND,String.format("Link with url: '%s' not found!", url), null, "Get Link", null);
            log.warning(exception.toString());
            throw exception;
        }
    }

//    public List<LinkBean> checkLinks(List<String> urls, String ipAddress) throws LinkException {
//        List<LinkBean> checkedLinks = new ArrayList<>();
//        if(!CollectionUtils.isEmpty(urls)) {
//            for (String url : urls) {
//                checkedLinks.add(checkLink(url, ipAddress));
//            }
//        }
//        return checkedLinks;
//    }

    private void performConnectionCheck(Link entity, RequestHistory request) {
        if (connectionValidationService.isValidConnection(entity.getUrl())) {
            whoIsService.setWhoIsDate(entity);
            log.info("URL CONNECTABLE, UNKNOWN STATUS");
            entity.setStatus(LinkStatus.SUSPICIOUS);
            requestHistoryService.processRequestHistory(request, entity);
        } else {
            log.info("URL CANT BE CONNECTED TO");
            entity.setStatus(LinkStatus.NOT_CONNECTABLE);
            requestHistoryService.processRequestHistory(request, entity);
        }
        entity.setSecurityLevel(-1);
        saveNewEntity(entity);
    }

    private boolean performConnectionCheck(Link entity) {
        if (connectionValidationService.isValidConnection(entity.getUrl())) {
            whoIsService.setWhoIsDate(entity);
            log.info("URL CONNECTABLE, UNKNOWN STATUS");
            entity.setStatus(LinkStatus.SUSPICIOUS);
        } else {
            log.info("URL CANT BE CONNECTED TO");
            entity.setStatus(LinkStatus.NOT_CONNECTABLE);
            return false;
        }
        entity.setSecurityLevel(-1);
        saveNewEntity(entity);
        return true;
    }

    // Will report a link
    public LinkBean reportLink(String url, String ipAddress, ReportType reportType) throws LinkException {
        log.info(REPORT_LINK_CD + " url = '" + url + "' received from " + ipAddress);
        requestHistoryService.ipCheck(ipAddress);
        RequestHistory request = requestHistoryService.saveRequestHistory(url, ipAddress, RequestType.REPORT);
        log.info(request.toString());

        List<String> errors = validationService.validateLinkRequest(url, ipAddress);
        boolean isValid = validateReportType(reportType);

        if (!CollectionUtils.isEmpty(errors) || (!ReportType.INVALID.equals(reportType) && !ReportType.VALID.equals(reportType))) {
            LinkException exception = new LinkException(HttpStatus.BAD_REQUEST, "Error occurred while validating request: " + url, null, ipAddress, errors);
            log.warning(exception.toString());
            throw exception;
        }

        String parsedUrl = parseUrlToDomainString(url);

        Optional<Link> retrievedEntity = repository.findById(parsedUrl);
        Link entity = null;

        if (!retrievedEntity.isPresent()) {
            entity = saveNewEntity(parsedUrl);
            performConnectionCheck(entity, request);
        } else if (retrievedEntity.get().getStatus().equals(LinkStatus.UNPROCCESSED)) {
            retrievedEntity.get().setUrl(parsedUrl);
            request.setUrl(parsedUrl);
            performConnectionCheck(retrievedEntity.get(), request);
        }

        requestHistoryService.processRequestHistory(request, nonNull(entity) ? entity : retrievedEntity.get());
        return convert(reportService.reportLink(parsedUrl, ipAddress, isValid));
    }

    // TODO: ALL PUBLIC FUNCTIONS BELOW ARE FOR ADMIN USE
    public LinkBean validateLink(String url) throws LinkException {
        // TODO: redo selenium validation LinkVerificationService.isConnectable()
        return null;
    }

    public List<LinkBean> getLinksByStatus(LinkStatus status) throws LinkException {
        List<Link> links = repository.findByStatus(status);
        if (links.isEmpty()) {

            LinkException exception = new LinkException(
                    HttpStatus.NOT_FOUND,
                    SERVICE_LOG_MESSAGE + "No links with status " + status + " found!",
                    null,"GET",null);
            throw exception;
        }
        return links.stream().map(LinkConverter::convert).collect(Collectors.toList());
    }

    public void updateLink(LinkUpdate request) throws LinkException {
        Optional<Link> found = repository.findById(request.getUrl());
        if (!found.isPresent()) {
            LinkException exception = new LinkException(
                    HttpStatus.NOT_FOUND,
                    SERVICE_LOG_MESSAGE + "Link not found!",
                    null,"PUT",null);
            throw exception;
        }

        Link link = found.get();
        link.setDescription(request.getDescription());
        link.setSecurityLevel(request.getSecurityLevel());
        link.setStatus(request.getStatus());
        repository.save(link);
    }

    public void updateLinks(LinkUpdateRequest request) {
        List<LinkUpdate> requests = request.getApprovalList();
        for (LinkUpdate req : requests) {
            if (req.isApprove()) {
                approveLink(req.getUrl(), req.getSecurityLevel());
            } else {
                invalidateLink(req.getUrl(), req.getSecurityLevel());
            }
        }
    }

    public void deleteLinkByUrl(String url) throws LinkException {
        Optional<Link> foundLink = repository.findById(url);
        if (!foundLink.isPresent()) {
            LinkException exception = new LinkException(HttpStatus.NOT_FOUND, "Link doesn't exist!", null, "DELETE", null);
            log.info(exception.toString());
            throw exception;
        }
        repository.delete(foundLink.get());
    }

    private void approveLink (String url, Integer securityLevel) {
        Optional<Link> link = repository.findById(url);
        if (link.isPresent()) {
            if(nonNull(securityLevel)) {
                link.get().setSecurityLevel(securityLevel);
            } else {
                link.get().setSecurityLevel(9);
            }
            link.get().setStatus(LinkStatus.VERIFIED);
            repository.save(link.get());
        }
    }

    private void invalidateLink (String url, Integer securityLevel) {
        Optional<Link> link = repository.findById(url);
        if (link.isPresent()) {
            if(nonNull(securityLevel)) {
                link.get().setSecurityLevel(securityLevel);
            } else {
                link.get().setSecurityLevel(-9);
            }
            link.get().setStatus(LinkStatus.BLACKLISTED);
            repository.save(link.get());
        }
    }

    public Link performMaintenance(String url) throws LinkException {
        Optional<Link> foundLink = repository.findById(url);
        if (!foundLink.isPresent()) {
            LinkException exception = new LinkException(HttpStatus.NOT_FOUND, "Url not found", null, "maintenance", null);
            log.warning(exception.toString());
            throw exception;
        }
        Link link = foundLink.get();
        System.setProperty("webdriver.chrome.driver", "resources\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");
        connectionValidationService.isValidConnectionJSoup(link);
        link.setLastMaintenance(new Timestamp(System.currentTimeMillis()));
        whoIsService.setWhoIsDate(link);
        repository.save(link);
        return link;
    }


    public void performMaintenance() {
        List<Link> allLinks = repository.findAll();
        List<String> allUrls = allLinks.stream().map(link -> link.getUrl()).collect(Collectors.toList());
        System.setProperty("webdriver.chrome.driver", "resources\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");
        allLinks.parallelStream().map(link -> {
            connectionValidationService.isValidConnectionJSoup(link);
            link.setLastMaintenance(new Timestamp(System.currentTimeMillis()));
            return link;
        }).collect(Collectors.toList());
        repository.saveAll(allLinks);

        List<Link> unknownLinks = allLinks.stream()
                .filter(link -> link.getStatus().equals(LinkStatus.NEED_INSPECTION))
                .collect(Collectors.toList());
        connectionValidationService.setIsConnectableSelenium(unknownLinks);
        repository.saveAll(unknownLinks);

    }

    // Service Private functions
    private void saveNewEntity(Link entity) {
        Timestamp currentTime = new Timestamp((System.currentTimeMillis()));
        if(isNull(entity.getCreationDate())) {
            entity.setCreationDate(currentTime);
        }
        entity.setModifiedDate(currentTime);
        repository.save(entity);
    }

    private Link saveNewEntity(String url) {
        Link entity = new Link();
        entity.setUrl(url);
        Timestamp currentTime = new Timestamp((System.currentTimeMillis()));
        if(isNull(entity.getCreationDate())) {
            entity.setCreationDate(currentTime);
        }
        entity.setModifiedDate(currentTime);
        repository.save(entity);
        return entity;
    }

    // TODO: delete when done with use;
    private void setAllDates() {
        List<Link> allLinks = repository.findAll();
        for (Link link : allLinks) {
            link.setCreationDate(new Date());
            repository.save(link);
        }
    }

    private void setLastModifiedInfo(Link entity) {
        if (isNull(entity.getCreationDate())) {
            entity.setCreationDate(new Date());
        }
        entity.setModifiedDate(new Date());
    }
}

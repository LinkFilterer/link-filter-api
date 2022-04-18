package com.koala.linkfilterapp.linkfilterapi.service.link.maintenance;

import com.koala.linkfilterapp.linkfilterapi.api.common.exception.CommonException;
import com.koala.linkfilterapp.linkfilterapi.api.link.dto.request.CreateLinkRequest;
import com.koala.linkfilterapp.linkfilterapi.api.link.dto.request.LinkSearchBean;
import com.koala.linkfilterapp.linkfilterapi.api.link.dto.request.LinkUpdate;
import com.koala.linkfilterapp.linkfilterapi.api.link.dto.request.LinkUpdateRequest;
import com.koala.linkfilterapp.linkfilterapi.api.link.dto.response.LinkBean;
import com.koala.linkfilterapp.linkfilterapi.api.link.entity.Link;
import com.koala.linkfilterapp.linkfilterapi.api.link.enums.LinkStatus;
import com.koala.linkfilterapp.linkfilterapi.repository.LinkRepository;
import com.koala.linkfilterapp.linkfilterapi.service.link.LinkConverter;
import com.koala.linkfilterapp.linkfilterapi.service.link.impl.LinkConnectionValidationService;
import com.koala.linkfilterapp.linkfilterapi.service.link.impl.LinkWhoIsService;
import org.dozer.DozerBeanMapper;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.koala.linkfilterapp.linkfilterapi.service.common.CommonApiConstants.SERVICE_LOG_MESSAGE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class LinkMaintenanceService {
    Logger log = Logger.getLogger("LinkMaintenanceService");

    @Autowired
    LinkRepository repository;

    @Autowired
    LinkConnectionValidationService connectionValidationService;

    @Autowired
    LinkWhoIsService whoIsService;

    public Link createLink(CreateLinkRequest request) {
        Link entity = new DozerBeanMapper().map(request, Link.class);
        setLastModifiedInfo(entity);
        whoIsService.setWhoIsDate(entity);
        Link res = repository.save(entity);
        log.info("Saved: " + entity);
        return res;
    }

    public Page<LinkBean> getLinks(LinkSearchBean searchBean) {
        Specification<Link> querySpec = createQuery(searchBean);
        Pageable pageRequest = getPageableRequest(searchBean);
        return repository.findAll(querySpec, pageRequest).map(LinkConverter::convert);
    }

    private Specification<Link> createQuery(LinkSearchBean searchBean) {
        return (root, query, builder) -> {
            final List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.hasText(searchBean.getUrl())) { predicates.add(builder.equal(root.<String>get("url"), searchBean.getUrl())); }
            if(nonNull(searchBean.getStatus())) { predicates.add(builder.equal(root.<String>get("status"), searchBean.getStatus())); }
            if(StringUtils.hasText(searchBean.getSecurityLevel())) { predicates.add(builder.equal(root.<String>get("securityLevel"), searchBean.getSecurityLevel())); }
            if(StringUtils.hasText(searchBean.getBadCount())) { predicates.add(builder.equal(root.<String>get("badCount"), searchBean.getBadCount())); }
            if(StringUtils.hasText(searchBean.getCreationDate())) { predicates.add(builder.equal(root.<String>get("creationDate"), searchBean.getCreationDate())); }
            if(StringUtils.hasText(searchBean.getModifiedDate())) { predicates.add(builder.equal(root.<String>get("modifiedDate"), searchBean.getModifiedDate())); }
            if(StringUtils.hasText(searchBean.getDescription())) { predicates.add(builder.equal(root.<String>get("description"), searchBean.getDescription())); }
            if(nonNull(searchBean.getIsConnectable())) { predicates.add(builder.equal(root.<String>get("isConnectable"), searchBean.getIsConnectable())); }
            if(nonNull(searchBean.getStatusCode())) { predicates.add(builder.equal(root.<String>get("statusCode"), searchBean.getStatusCode())); }
            if(StringUtils.hasText(searchBean.getLastMaintenance())) { predicates.add(builder.equal(root.<String>get("lastMaintenance"), searchBean.getLastMaintenance())); }
            if(StringUtils.hasText(searchBean.getWhoIsDate())) { predicates.add(builder.equal(root.<String>get("whoIsDate"), searchBean.getWhoIsDate())); }
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    private Pageable getPageableRequest(LinkSearchBean searchBean) {
        Sort sortOrder = Sort.by(searchBean.getSortType().toString());
        Optional<Sort.Direction> sortDirection = Sort.Direction.fromOptionalString(searchBean.getSortDir());
        if (sortDirection.isPresent()) {
            sortOrder = Sort.by(sortDirection.get(), searchBean.getSortType().toString());
        }

        return PageRequest.of(searchBean.getPageNo(), searchBean.getPageSize(), sortOrder);
    }


    // TODO: ALL FUNCTIONS BELOW ARE FOR ADMIN USE
    public LinkBean validateLink(String url) throws CommonException {
        // TODO: redo selenium validation LinkVerificationService.isConnectable()
        return null;
    }

    public List<LinkBean> getLinksByStatus(LinkStatus status) throws CommonException {
        List<Link> links = repository.findByStatus(status);
        if (links.isEmpty()) {

            CommonException exception = new CommonException(
                    HttpStatus.NOT_FOUND,
                    SERVICE_LOG_MESSAGE + "No links with status " + status + " found!",
                    null,"GET",null);
            throw exception;
        }
        return links.stream().map(LinkConverter::convert).collect(Collectors.toList());
    }

    public void updateLink(LinkUpdate request) throws CommonException {
        Optional<Link> found = repository.findById(request.getUrl());
        if (!found.isPresent()) {
            CommonException exception = new CommonException(
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

    public void deleteLinkByUrl(String url) throws CommonException {
        Optional<Link> foundLink = repository.findById(url);
        if (!foundLink.isPresent()) {
            CommonException exception = new CommonException(HttpStatus.NOT_FOUND, "Link doesn't exist!", null, "DELETE", null);
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

    public Link performMaintenance(String url) throws CommonException {
        Optional<Link> foundLink = repository.findById(url);
        if (!foundLink.isPresent()) {
            CommonException exception = new CommonException(HttpStatus.NOT_FOUND, "Url not found", null, "maintenance", null);
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

    private void setLastModifiedInfo(Link entity) {
        if (isNull(entity.getCreationDate())) {
            entity.setCreationDate(new Date());
        }
        entity.setModifiedDate(new Date());
    }
}

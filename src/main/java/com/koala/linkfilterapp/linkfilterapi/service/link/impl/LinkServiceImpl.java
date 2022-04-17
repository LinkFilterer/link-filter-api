package com.koala.linkfilterapp.linkfilterapi.service.link.impl;

import com.koala.linkfilterapp.linkfilterapi.api.requesthistory.entity.RequestHistory;
import com.koala.linkfilterapp.linkfilterapi.api.link.enums.LinkStatus;
import com.koala.linkfilterapp.linkfilterapi.api.report.enums.ReportType;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.CommonException;
import com.koala.linkfilterapp.linkfilterapi.api.link.dto.response.LinkBean;
import com.koala.linkfilterapp.linkfilterapi.api.link.entity.Link;
import com.koala.linkfilterapp.linkfilterapi.repository.LinkRepository;
import com.koala.linkfilterapp.linkfilterapi.service.ipaddress.impl.RequestHistoryServiceImpl;
import com.koala.linkfilterapp.linkfilterapi.service.link.LinkService;
import com.koala.linkfilterapp.linkfilterapi.service.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static com.koala.linkfilterapp.linkfilterapi.service.common.CommonApiConstants.CHECK_LINK_CD;
import static com.koala.linkfilterapp.linkfilterapi.service.common.CommonApiConstants.REPORT_LINK_CD;
import static com.koala.linkfilterapp.linkfilterapi.service.link.LinkConverter.convert;
import static com.koala.linkfilterapp.linkfilterapi.service.link.validator.LinkValidator.parseUrlToDomainString;
import static com.koala.linkfilterapp.linkfilterapi.service.link.validator.LinkValidator.validateReportType;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class LinkServiceImpl implements LinkService {
    Logger log = Logger.getLogger("LinkService");

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

    // Will only check the database for results
    public LinkBean checkLink(String url, String ipAddress, RequestHistory request) throws CommonException {
        log.info(CHECK_LINK_CD + " url = '" + url + "' received from " + ipAddress);


        List<String> errors = validationService.validateLinkRequest(url, ipAddress);
        if (!CollectionUtils.isEmpty(errors)) {
            CommonException exception = new CommonException(HttpStatus.BAD_REQUEST, "Error occurred while validating request: " + url, null, ipAddress, errors);
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

        log.info("URL FOUND");
        requestHistoryService.processRequestHistory(request, nonNull(entity) ? entity : retrievedEntity.get());
        return convert(nonNull(entity) ? entity : retrievedEntity.get());
    }

    public List<LinkBean> checkLinks(List<String> urls, String ipAddress, RequestHistory requestHistory) throws CommonException {
        List<LinkBean> checkedLinks = new ArrayList<>();
        if(!CollectionUtils.isEmpty(urls)) {
            for (String url : urls) {
                checkedLinks.add(checkLink(url, ipAddress, requestHistory));
            }
        }
        return checkedLinks;
    }

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

    // Will report a link
    public LinkBean reportLink(String url, String ipAddress, ReportType reportType, RequestHistory request) throws CommonException {
        log.info(REPORT_LINK_CD + " url = '" + url + "' received from " + ipAddress);
        requestHistoryService.ipCheck(ipAddress);
        log.info(request.toString());

        List<String> errors = validationService.validateLinkRequest(url, ipAddress);
        boolean isValid = validateReportType(reportType);

        if (!CollectionUtils.isEmpty(errors) || (!ReportType.INVALID.equals(reportType) && !ReportType.VALID.equals(reportType))) {
            CommonException exception = new CommonException(HttpStatus.BAD_REQUEST, "Error occurred while validating request: " + url, null, ipAddress, errors);
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
}

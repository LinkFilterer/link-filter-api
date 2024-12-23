package com.koala.linkfilterapp.linkfilterapi.service.link.impl;

import com.koala.linkfilterapp.linkfilterapi.api.common.exception.CommonException;
import com.koala.linkfilterapp.linkfilterapi.api.link.dto.response.LinkBean;
import com.koala.linkfilterapp.linkfilterapi.api.link.entity.Link;
import com.koala.linkfilterapp.linkfilterapi.api.link.enums.LinkStatus;
import com.koala.linkfilterapp.linkfilterapi.api.report.enums.ReportType;
import com.koala.linkfilterapp.linkfilterapi.api.requesthistory.entity.RequestHistory;
import com.koala.linkfilterapp.linkfilterapi.repository.LinkRepository;
import com.koala.linkfilterapp.linkfilterapi.service.ipaddress.impl.RequestHistoryServiceImpl;
import com.koala.linkfilterapp.linkfilterapi.service.link.LinkConverter;
import com.koala.linkfilterapp.linkfilterapi.service.link.LinkService;
import com.koala.linkfilterapp.linkfilterapi.service.report.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.naming.LinkException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.koala.linkfilterapp.linkfilterapi.service.common.CommonApiConstants.*;
import static com.koala.linkfilterapp.linkfilterapi.service.link.LinkConverter.convert;
import static com.koala.linkfilterapp.linkfilterapi.service.link.validator.LinkValidator.parseUrlToDomainString;
import static com.koala.linkfilterapp.linkfilterapi.service.link.validator.LinkValidator.validateReportType;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@Slf4j
public class LinkServiceImpl implements LinkService {

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

    // Will only check the database for results
    public LinkBean checkLink(String url, String ipAddress, RequestHistory request) throws CommonException {
        log.info(CHECK_LINK_CD + " url = '" + url + "' received from " + ipAddress);
        String parsedUrl = parseUrlToDomainString(url);

        Optional<Link> retrievedEntity = repository.findByUrl(parsedUrl);
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
        return convert(nonNull(entity) ? entity : retrievedEntity.get());
    }

    @Scheduled(fixedRate = 300000)
    @CacheEvict(value = "linkUrls", allEntries = true)
    public void evictLinkCaches() {
    }

    public List<LinkBean> checkLinks(List<String> urls, String ipAddress, List<RequestHistory> requestHistories) throws CommonException {
        log.info(CHECK_LINK_CD + " url = '" + urls + "' received from " + ipAddress);
        Map<String, String> unparsedToParsedUrlMap = new HashMap<>();

        List<String> parsedUrls = urls.stream().distinct().map(url -> {
            try {
                String parsed = parseUrlToDomainString(url);
                unparsedToParsedUrlMap.put(parsed, url);
                return parsed;
            } catch (CommonException e) {
                log.error(e.toString());
                return null;
            }
        }).collect(Collectors.toList());

        List<Link> foundLinks = repository.findAllById(parsedUrls);
        List<String> foundUrls = foundLinks.stream().map(Link::getUrl).collect(Collectors.toList());
        List<String> unfoundUrls = parsedUrls.stream().filter(url -> !foundUrls.contains(url)).collect(Collectors.toList());
        List<Link> newEntities = new ArrayList<>();

        for (String unfoundUrl : unfoundUrls) {
            Optional<RequestHistory> mappedRequest = requestHistories.stream().filter(request -> request.getRequestedUrl().equals(unparsedToParsedUrlMap.get(unfoundUrl))).findAny();
            Link entity = saveNewEntity(unfoundUrl);
            performConnectionCheck(entity, mappedRequest.get());
            newEntities.add(entity);
        }

        for (Link link : foundLinks) {
            String unparsedUrl = unparsedToParsedUrlMap.get(link.getUrl());
            Optional<RequestHistory> mappedRequest = requestHistories.stream().filter(request ->
                            request.getRequestedUrl().equals(unparsedUrl))
                    .findAny();

            if (isNull(link.getStatus()) || link.getStatus().equals(LinkStatus.UNPROCCESSED)) {
                if (mappedRequest.isPresent()) {
                    mappedRequest.get().setUrl(link.getUrl());
                    performConnectionCheck(link, mappedRequest.get());
                }
            }
        }
        foundLinks.addAll(newEntities);
        return foundLinks.stream().map(LinkConverter::convert).collect(Collectors.toList());
    }

    private void performConnectionCheck(Link entity, RequestHistory request) {
        if (connectionValidationService.isValidConnection(entity.getUrl())) {
            whoIsService.setWhoIsDate(entity);
            entity.setStatus(LinkStatus.SUSPICIOUS);
            requestHistoryService.processRequestHistory(request, entity);
        } else {
            entity.setStatus(LinkStatus.NOT_CONNECTABLE);
            requestHistoryService.processRequestHistory(request, entity);
        }
        entity.setSecurityLevel(-1);
        saveNewEntity(entity);
    }

    // Will report a link
    public LinkBean reportLink(String url, String ipAddress, String userId, ReportType reportType, RequestHistory request) throws CommonException {
        log.info(REPORT_LINK_CD + " url = '" + url + "' received from " + ipAddress);
        requestHistoryService.ipCheck(ipAddress, userId);
        log.info(request.toString());


        String parsedUrl = parseUrlToDomainString(url);
        boolean isValid = validateReportType(reportType);
        if (checkIfVerified(url)) {
            throwAlreadyVerifiedException();
        }

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
        return convert(reportService.reportLink(parsedUrl, ipAddress, userId, isValid));
    }

    private void throwAlreadyVerifiedException() throws CommonException {
        CommonException exception = new CommonException(HttpStatus.OK, "Link has already been verified by the team");
        throw exception;
    }

    private boolean checkIfVerified(String url) {
        Optional<Link> link = repository.findById(url);
        return link.filter(value -> value.getSecurityLevel() > 8 || LinkStatus.VERIFIED.equals(value.getStatus())).isPresent();
    }

    private void saveNewEntity(Link entity) {
        Timestamp currentTime = new Timestamp((System.currentTimeMillis()));
        if (isNull(entity.getCreationDate())) {
            entity.setCreationDate(currentTime);
        }
        entity.setModifiedDate(currentTime);
        repository.save(entity);
    }

    private Link saveNewEntity(String url) {
        Link entity = new Link();
        entity.setUrl(url);
        Timestamp currentTime = new Timestamp((System.currentTimeMillis()));
        if (isNull(entity.getCreationDate())) {
            entity.setCreationDate(currentTime);
        }
        entity.setModifiedDate(currentTime);
        repository.save(entity);
        return entity;
    }
}

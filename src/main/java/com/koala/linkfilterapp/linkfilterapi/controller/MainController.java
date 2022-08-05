package com.koala.linkfilterapp.linkfilterapi.controller;

import com.koala.linkfilterapp.linkfilterapi.api.common.dto.response.RestResponse;
import com.koala.linkfilterapp.linkfilterapi.api.common.enums.AddressType;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.CommonException;
import com.koala.linkfilterapp.linkfilterapi.api.link.dto.response.LinkBean;
import com.koala.linkfilterapp.linkfilterapi.api.report.enums.ReportType;
import com.koala.linkfilterapp.linkfilterapi.api.requesthistory.entity.RequestHistory;
import com.koala.linkfilterapp.linkfilterapi.api.requesthistory.enums.RequestType;
import com.koala.linkfilterapp.linkfilterapi.api.sponsor.dto.response.SponsorBean;
import com.koala.linkfilterapp.linkfilterapi.service.ipaddress.impl.IpAddressServiceImpl;
import com.koala.linkfilterapp.linkfilterapi.service.ipaddress.impl.RequestHistoryServiceImpl;
import com.koala.linkfilterapp.linkfilterapi.service.link.impl.LinkServiceImpl;
import com.koala.linkfilterapp.linkfilterapi.service.sponsor.impl.SponsorServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.koala.linkfilterapp.linkfilterapi.controller.common.ControllerConstants.BROWSER_EXTENSION_ORIGIN;
import static com.koala.linkfilterapp.linkfilterapi.controller.common.ControllerConstants.UI_SERVER_ORIGIN;

@CrossOrigin(origins = {UI_SERVER_ORIGIN, BROWSER_EXTENSION_ORIGIN})
@RestController
@Log4j2
@RequestMapping("/api/common")
public class MainController {

    @Autowired
    LinkServiceImpl linkService;

    @Autowired
    SponsorServiceImpl sponsorService;

    @Autowired
    IpAddressServiceImpl ipAddressService;

    @Autowired
    RequestHistoryServiceImpl requestHistoryService;

    // Will simply check database for a result and return it (INVALID/VALID/UNKNOWN) is public


    @PostMapping(value = "/checkLink")
    public ResponseEntity<RestResponse<LinkBean>> checkLink(@RequestParam String url,
                                                            HttpServletRequest request) throws CommonException {
        String ipAddress = request.getRemoteAddr();
        if (ipAddressService.checkIfBanned(ipAddress, AddressType.WEB, Strings.EMPTY)) {
            return null;
        }
        RequestHistory requestHistory = requestHistoryService.saveRequestHistory(url, ipAddress, Strings.EMPTY, RequestType.CHECK, AddressType.WEB);
        requestHistoryService.ipCheck(request.getRemoteAddr());
        log.info(String.format("Request: %s", request.getAuthType()));
        LinkBean response = linkService.checkLink(url, request.getRemoteAddr(), requestHistory);
        SponsorBean sponsor = sponsorService.getSponsorInfo();
        response.setSponsor(sponsor);
        log.info(String.format("Sending Response to %s: %s", request.getRemoteAddr(), response));
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Link Checked", response, null), HttpStatus.OK);
    }

    @PostMapping(value = "/reportLink")
    public ResponseEntity<RestResponse<LinkBean>> reportLink(@RequestParam String url,
                                                             @RequestParam ReportType reportType,
                                                             @RequestParam(required = false) String userId,
                                                             HttpServletRequest request) throws CommonException {
        String ipAddress = request.getRemoteAddr();
        if (ipAddressService.checkIfBanned(ipAddress, AddressType.WEB, Strings.EMPTY)) {
            return null;
        }
        RequestHistory requestHistory = requestHistoryService.saveRequestHistory(url, ipAddress, Strings.EMPTY, RequestType.REPORT, AddressType.WEB);
        LinkBean response = linkService.reportLink(url, request.getRemoteAddr(), userId, reportType, requestHistory);
        log.info(String.format("Sending Response to %s: %s", request.getRemoteAddr(), response));
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Link Reported", response, null), HttpStatus.OK);
    }
}

package com.koala.linkfilterapp.linkfilterapi.controller;

import com.koala.linkfilterapp.linkfilterapi.api.common.dto.response.RestResponse;
import com.koala.linkfilterapp.linkfilterapi.api.report.enums.ReportType;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.LinkException;
import com.koala.linkfilterapp.linkfilterapi.api.link.dto.response.LinkBean;
import com.koala.linkfilterapp.linkfilterapi.api.sponsor.dto.response.SponsorBean;
import com.koala.linkfilterapp.linkfilterapi.service.ipaddress.impl.IpAddressServiceImpl;
import com.koala.linkfilterapp.linkfilterapi.service.ipaddress.impl.RequestHistoryServiceImpl;
import com.koala.linkfilterapp.linkfilterapi.service.link.impl.LinkServiceImpl;
import com.koala.linkfilterapp.linkfilterapi.service.sponsor.impl.SponsorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

import static com.koala.linkfilterapp.linkfilterapi.controller.ControllerConstants.BROWSER_EXTENSION_ORIGIN;
import static com.koala.linkfilterapp.linkfilterapi.controller.ControllerConstants.UI_SERVER_ORIGIN;

@CrossOrigin(origins = {UI_SERVER_ORIGIN, BROWSER_EXTENSION_ORIGIN})
@RestController
public class LinkController {
    Logger log = Logger.getLogger("LinkController");

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
    public ResponseEntity<RestResponse<LinkBean>> checkLink(@RequestParam String url, HttpServletRequest request) throws LinkException {
        if (ipAddressService.checkIfBanned(request.getRemoteAddr())) {
            return null;
        }
        log.info(String.format("Request: %s", request.getAuthType()));
        LinkBean response = linkService.checkLink(url, request.getRemoteAddr());
        log.info("before get sponser info");
        SponsorBean sponsor = sponsorService.getSponsorInfo();
        log.info("after get sponser info");
        response.setSponsor(sponsor);
        log.info(String.format("Sending Response to %s: %s", request.getRemoteAddr(), response));
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Link Checked", response, null), HttpStatus.OK);
    }

    @PostMapping(value = "/reportLink")
    public ResponseEntity<RestResponse<LinkBean>> reportLink(@RequestParam String url, @RequestParam ReportType reportType, HttpServletRequest request) throws LinkException {
        if (ipAddressService.checkIfBanned(request.getRemoteAddr())) {
            return null;
        }
        LinkBean response = linkService.reportLink(url, request.getRemoteAddr(), reportType);
        log.info(String.format("Sending Response to %s: %s", request.getRemoteAddr(), response));
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Link Reported", response, null), HttpStatus.OK);
    }

}

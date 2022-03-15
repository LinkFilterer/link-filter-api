package com.koala.linkfilterapp.linkfilterapi.controller;

import com.koala.linkfilterapp.linkfilterapi.api.common.dto.response.RestResponse;
import com.koala.linkfilterapp.linkfilterapi.api.sponsor.dto.response.SponsorBean;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.LinkException;
import com.koala.linkfilterapp.linkfilterapi.service.common.impl.IpAddressServiceImpl;
import com.koala.linkfilterapp.linkfilterapi.service.sponsor.impl.SponsorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class SponsorController {
    Logger log = Logger.getLogger("SponsorController");

    @Autowired
    SponsorServiceImpl sponsorService;

    @Autowired
    IpAddressServiceImpl ipAddressService;

    @GetMapping (value = "/getSponsor")
    public ResponseEntity<RestResponse<SponsorBean>> getSponsor(HttpServletRequest request) throws LinkException {
        if (ipAddressService.checkIfBanned(request.getRemoteAddr())) {
            return null;
        }

        log.info(String.format("Request: %s", request.getAuthType()));
        SponsorBean response = sponsorService.getSponsorInfo();
        log.info(String.format("Sending Response to %s: %s", request.getRemoteAddr(), response));
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Sponsor Fetched", response, null), HttpStatus.OK);
    }
}

package com.koala.linkfilterapp.linkfilterapi.controller.maintenance;

import com.koala.linkfilterapp.linkfilterapi.api.common.dto.response.RestResponse;
import com.koala.linkfilterapp.linkfilterapi.api.common.enums.SponsorSortType;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.LinkException;
import com.koala.linkfilterapp.linkfilterapi.api.sponsor.dto.request.SponsorRequestBean;
import com.koala.linkfilterapp.linkfilterapi.api.sponsor.entity.Sponsor;
import com.koala.linkfilterapp.linkfilterapi.service.common.impl.IpAddressServiceImpl;
import com.koala.linkfilterapp.linkfilterapi.service.sponsor.impl.SponsorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.logging.Logger;

import static com.koala.linkfilterapp.linkfilterapi.controller.ControllerConstants.UI_SERVER_ORIGIN;

@CrossOrigin(origins = UI_SERVER_ORIGIN)
@RestController
public class SponsorMaintenanceController {
    Logger log = Logger.getLogger("SponsorMaintenanceController");

    @Autowired
    SponsorServiceImpl sponsorService;

    @Autowired
    IpAddressServiceImpl ipAddressService;

    @GetMapping (value = "/maintenance/getAllSponsors")
    public ResponseEntity<RestResponse<List<Sponsor>>> getAllSponsors(HttpServletRequest request) throws LinkException {
        if (ipAddressService.checkIfBanned(request.getRemoteAddr())) {
            return null;
        }

        log.info(String.format("Request: %s", request.getAuthType()));
        List<Sponsor> res = sponsorService.getAllSponsors();
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Sponsors fetched", res, null), HttpStatus.OK);
    }

    @GetMapping (value = "/maintenance/searchSponsors")
    public ResponseEntity<RestResponse<Page<Sponsor>>> searchSponsors(@RequestParam String projectName, @RequestParam Integer page, @RequestParam Integer size, @RequestParam SponsorSortType sortType, HttpServletRequest request) throws LinkException {
        if (ipAddressService.checkIfBanned(request.getRemoteAddr())) {
            return null;
        }

        log.info(String.format("Request: %s", request.getAuthType()));
        Page<Sponsor> res = sponsorService.searchSponsors(page, size, sortType, projectName);
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Sponsors searched", res, null), HttpStatus.OK);
    }

    @PostMapping(value = "/maintenance/postSponsor")
    public ResponseEntity<RestResponse<Sponsor>> postSponsor(@RequestBody SponsorRequestBean bean, HttpServletRequest request) throws LinkException {
        if (ipAddressService.checkIfBanned(request.getRemoteAddr())) {
            return null;
        }

        log.info(String.format("Request: %s", request.getAuthType()));
        Sponsor response = sponsorService.createSponsor(bean);
        log.info(String.format("Sending Response to %s: %s", request.getRemoteAddr(), response));
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.CREATED.toString(), "Sponsor Created", response, null), HttpStatus.CREATED);
    }

    @PutMapping(value = "/maintenance/updateSponsor")
    public ResponseEntity<RestResponse<Sponsor>> updateSponsor(@RequestBody SponsorRequestBean bean, HttpServletRequest request) throws LinkException {
        if (ipAddressService.checkIfBanned(request.getRemoteAddr())) {
            return null;
        }
        log.info(bean.toString());
        log.info(String.format("Request: %s", request.getAuthType()));
        Sponsor response = sponsorService.updateSponsor(bean);
        log.info(String.format("Sending Response to %s: %s", request.getRemoteAddr(), response));
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Sponsor Updated", response, null), HttpStatus.OK);
    }

    @DeleteMapping (value = "/maintenance/deleteSponsor")
    public ResponseEntity<RestResponse<String>> deleteSponsor(String projectName, HttpServletRequest request) throws LinkException {
        if (ipAddressService.checkIfBanned(request.getRemoteAddr())) {
            return null;
        }

        log.info(String.format("Request: %s", request.getAuthType()));
        sponsorService.deleteSponsor(projectName);
        log.info(String.format("Sending Response to %s: %s", request.getRemoteAddr(), projectName));
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Sponsor deleted", projectName, null), HttpStatus.OK);
    }
}

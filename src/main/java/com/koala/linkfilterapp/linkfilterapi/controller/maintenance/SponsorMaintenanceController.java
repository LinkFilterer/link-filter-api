package com.koala.linkfilterapp.linkfilterapi.controller.maintenance;

import com.koala.linkfilterapp.linkfilterapi.api.common.dto.response.RestResponse;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.CommonException;
import com.koala.linkfilterapp.linkfilterapi.api.sponsor.dto.request.SponsorRequestBean;
import com.koala.linkfilterapp.linkfilterapi.api.sponsor.dto.request.SponsorSearchBean;
import com.koala.linkfilterapp.linkfilterapi.api.sponsor.entity.Sponsor;
import com.koala.linkfilterapp.linkfilterapi.api.sponsor.enums.SponsorSortType;
import com.koala.linkfilterapp.linkfilterapi.service.ipaddress.impl.IpAddressServiceImpl;
import com.koala.linkfilterapp.linkfilterapi.service.sponsor.impl.SponsorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

import static com.koala.linkfilterapp.linkfilterapi.controller.common.ControllerConstants.UI_SERVER_ORIGIN;

@CrossOrigin(origins = UI_SERVER_ORIGIN)
@RestController
public class SponsorMaintenanceController {
    Logger log = Logger.getLogger("SponsorMaintenanceController");

    @Autowired
    SponsorServiceImpl sponsorService;

    @Autowired
    IpAddressServiceImpl ipAddressService;

    @GetMapping (value = "/maintenance/searchSponsors")
    public ResponseEntity<RestResponse<Page<Sponsor>>> searchSponsors(
            @RequestParam(required = false) String projectName,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String banner,
            @RequestParam(required = false) String url,
            @RequestParam(required = false) String creationDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Boolean isExpired,
            @RequestParam(required = false) Double weight,
            @RequestParam(required = false, defaultValue = "creationDate") SponsorSortType sortType,
            @RequestParam(required = false, defaultValue = "asc") String sortDir,
            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            HttpServletRequest request) throws CommonException {
        if (ipAddressService.checkIfBanned(request.getRemoteAddr())) {
            return null;
        }

        log.info(String.format("Request: %s", request.getAuthType()));

        SponsorSearchBean searchBean = SponsorSearchBean.builder()
                .id(projectName)
                .description(description)
                .banner(banner)
                .url(url)
                .creationDate(creationDate)
                .endDate(endDate)
                .isExpired(isExpired)
                .weight(weight)
                .sortType(sortType)
                .sortDir(sortDir)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .build();


        Page<Sponsor> res = sponsorService.searchSponsors(searchBean);
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Sponsors searched", res, null), HttpStatus.OK);
    }

    @PostMapping(value = "/maintenance/postSponsor")
    public ResponseEntity<RestResponse<Sponsor>> postSponsor(@RequestBody SponsorRequestBean bean, HttpServletRequest request) throws CommonException {
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
    public ResponseEntity<RestResponse<Sponsor>> updateSponsor(@RequestBody SponsorRequestBean bean, HttpServletRequest request) throws CommonException {
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
    public ResponseEntity<RestResponse<String>> deleteSponsor(String projectName, HttpServletRequest request) throws CommonException {
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

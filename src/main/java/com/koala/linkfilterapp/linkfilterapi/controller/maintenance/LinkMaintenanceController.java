package com.koala.linkfilterapp.linkfilterapi.controller.maintenance;

import com.koala.linkfilterapp.linkfilterapi.api.common.dto.response.RestResponse;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.CommonException;
import com.koala.linkfilterapp.linkfilterapi.api.link.dto.request.*;
import com.koala.linkfilterapp.linkfilterapi.api.link.dto.response.LinkBean;
import com.koala.linkfilterapp.linkfilterapi.api.link.entity.Link;
import com.koala.linkfilterapp.linkfilterapi.api.link.enums.LinkSortType;
import com.koala.linkfilterapp.linkfilterapi.api.link.enums.LinkStatus;
import com.koala.linkfilterapp.linkfilterapi.service.link.impl.LinkServiceImpl;
import com.koala.linkfilterapp.linkfilterapi.service.link.maintenance.LinkMaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.logging.Logger;

import static com.koala.linkfilterapp.linkfilterapi.controller.common.ControllerConstants.UI_SERVER_ORIGIN;
import static com.koala.linkfilterapp.linkfilterapi.service.link.LinkConverter.convert;

@CrossOrigin(origins = UI_SERVER_ORIGIN)
@RestController
@RequestMapping("/maintenance")
public class LinkMaintenanceController {
    Logger log = Logger.getLogger("LinkMaintenanceController");

    @Autowired
    LinkServiceImpl linkService;

    @Autowired
    LinkMaintenanceService maintenanceService;

    @PostMapping(value = "/createLink")
    public ResponseEntity<RestResponse<Link>> createLink(
            @RequestBody CreateLinkRequest request) {
        log.info("Request received: " + request);
        Link response = maintenanceService.createLink(request);
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.CREATED.toString(), "Link successfully created", response, null), HttpStatus.CREATED);
    }

    @GetMapping(value = "/searchLinks")
    public ResponseEntity<RestResponse<Page<LinkBean>>> searchLinks(
            @RequestParam(required = false) String url,
            @RequestParam(required = false) LinkStatus status,
            @RequestParam(required = false) String securityLevel,
            @RequestParam(required = false) String badCount,
            @RequestParam(required = false) String creationDate,
            @RequestParam(required = false) String modifiedDate,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Boolean isConnectable,
            @RequestParam(required = false) Integer statusCode,
            @RequestParam(required = false) String lastMaintenance,
            @RequestParam(required = false) String whoIsDate,
            @RequestParam(required = false, defaultValue = "modifiedDate") LinkSortType sortType,
            @RequestParam(required = false, defaultValue = "asc") String sortDir,
            @RequestParam(required = false, defaultValue = "0") Integer pagoNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            HttpServletRequest request) throws CommonException {
        LinkSearchBean searchBean = LinkSearchBean.builder()
                .url(url).status(status).securityLevel(securityLevel)
                .badCount(badCount).creationDate(creationDate).modifiedDate(modifiedDate)
                .description(description).isConnectable(isConnectable).statusCode(statusCode)
                .lastMaintenance(lastMaintenance).whoIsDate(whoIsDate)
                .sortType(sortType).sortDir(sortDir).pageNo(pagoNo).pageSize(pageSize)
                .build();
        Page<LinkBean> response = maintenanceService.getLinks(searchBean);
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Successfully retrieved links", response, null), HttpStatus.OK);
    }

    @PostMapping(value = "/checkLinks")
    public ResponseEntity<RestResponse<List<LinkBean>>> postLink(@RequestBody CheckLinksRequest checkRequest, HttpServletRequest request) throws CommonException {
        String ipAddress = request.getRemoteAddr();
        log.info(String.format("Request: %s", request.getAuthType()));

        List<LinkBean> response = linkService.checkLinks(checkRequest.getUrls(), request.getRemoteAddr(), null);

        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Successfully checked links", response, null), HttpStatus.OK);
    }

    @PutMapping(value = "/updateLink")
    public ResponseEntity<RestResponse<String>> updateLink(
            @RequestBody LinkUpdate request) throws CommonException {
        maintenanceService.updateLink(request);
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Successfully updated Link", null, null), HttpStatus.OK);
    }

    @PutMapping(value = "/updateLinks")
    public ResponseEntity<RestResponse<String>> updateLinks(
            @RequestBody LinkUpdateRequest request) throws CommonException {
        maintenanceService.updateLinks(request);
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Successfully updated Link", null, null), HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteLinkByUrl")
    public ResponseEntity<RestResponse<String>> deleteLink(
            @RequestParam String url) throws CommonException {
        maintenanceService.deleteLinkByUrl(url);

        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Successfully Deleted Link Data", url, null), HttpStatus.OK);
    }

    // Will use selenium to validate a link (resource intensive!!!)
    @PostMapping(value = "/validateLink")
    public ResponseEntity<RestResponse<LinkBean>> postLink(@RequestParam String url) throws CommonException {
        LinkBean response = maintenanceService.validateLink(url);

        return new ResponseEntity<RestResponse<LinkBean>>(
                new RestResponse<LinkBean>(HttpStatus.OK.toString(), "Successfully Posted Link", response, null), HttpStatus.OK);
    }

    @PutMapping(value = "/maintainLink")
    public ResponseEntity<RestResponse<LinkBean>> performLinkMaintenance(@RequestParam String url) throws CommonException {
        Link link = maintenanceService.performMaintenance(url);
        return new ResponseEntity<RestResponse<LinkBean>>(
                new RestResponse<LinkBean>(HttpStatus.OK.toString(), "Successfully performed maintenance}", convert(link), null), HttpStatus.OK);
    }

    @PutMapping(value = "/performMaintenance")
    public ResponseEntity<RestResponse<LinkBean>> performLinkMaintenance() throws CommonException {
        maintenanceService.performMaintenance();
        return new ResponseEntity<RestResponse<LinkBean>>(
                new RestResponse<LinkBean>(HttpStatus.OK.toString(), "Successfully performed maintenance}", null, null), HttpStatus.OK);
    }
}

package com.koala.linkfilterapp.linkfilterapi.controller.maintenance;

import com.koala.linkfilterapp.linkfilterapi.api.common.dto.response.RestResponse;
import com.koala.linkfilterapp.linkfilterapi.api.common.enums.LinkSortType;
import com.koala.linkfilterapp.linkfilterapi.api.common.enums.LinkStatus;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.LinkException;
import com.koala.linkfilterapp.linkfilterapi.api.link.dto.request.CheckLinksRequest;
import com.koala.linkfilterapp.linkfilterapi.api.link.dto.request.CreateLinkRequest;
import com.koala.linkfilterapp.linkfilterapi.api.link.dto.request.LinkUpdate;
import com.koala.linkfilterapp.linkfilterapi.api.link.dto.request.LinkUpdateRequest;
import com.koala.linkfilterapp.linkfilterapi.api.link.dto.response.LinkBean;
import com.koala.linkfilterapp.linkfilterapi.api.link.dto.response.LinkCollection;
import com.koala.linkfilterapp.linkfilterapi.api.link.entity.Link;
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
import java.util.stream.Collectors;

import static com.koala.linkfilterapp.linkfilterapi.controller.ControllerConstants.UI_SERVER_ORIGIN;
import static com.koala.linkfilterapp.linkfilterapi.service.link.LinkConverter.convert;

@CrossOrigin(origins = UI_SERVER_ORIGIN)
@RestController
public class LinkMaintenanceController {
    Logger log = Logger.getLogger("LinkMaintenanceController");

    @Autowired
    LinkServiceImpl linkService;

    @Autowired
    LinkMaintenanceService maintenanceService;

    @PostMapping(value ="/maintenance/createLink")
    public ResponseEntity<RestResponse<Link>> createLink(
            @RequestBody CreateLinkRequest request) {
        log.info("Request received: " + request);
        Link response = maintenanceService.createLink(request);
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.CREATED.toString(),"Link successfully created", response, null), HttpStatus.CREATED);
    }

    @GetMapping(value = "/maintenance/getAllLinks")
    public ResponseEntity<RestResponse<Page<LinkBean>>> getAllLinks(
            @RequestParam Integer page, @RequestParam Integer size, @RequestParam LinkSortType sortType, HttpServletRequest request) throws LinkException {
        Page<LinkBean> response = maintenanceService.getAllLinks(page, size, sortType);
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Successfully retrieved links", response, null), HttpStatus.OK);
    }

    @GetMapping(value = "/maintenance/searchLink")
    public ResponseEntity<RestResponse<Page<LinkBean>>> searchLink(@RequestParam String url, @RequestParam Integer page, @RequestParam Integer size, @RequestParam LinkSortType sortType, HttpServletRequest request) throws LinkException {
        Page<LinkBean> response = maintenanceService.searchLink(page, size, sortType, url);
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Successfully retrieved links", response, null), HttpStatus.OK);
    }

    @PostMapping(value = "/maintenance/checkLinks")
    public ResponseEntity<RestResponse<List<LinkBean>>> postLink(@RequestBody CheckLinksRequest checkRequest, HttpServletRequest request) throws LinkException {
        List<LinkBean> response = linkService.checkLinks(checkRequest.getUrls(), request.getRemoteAddr());

        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Successfully checked links", response, null), HttpStatus.OK);
    }

    @GetMapping(value = "/maintenance/getLinkByStatus")
    public ResponseEntity<RestResponse<LinkCollection>> updateLink(
            @RequestParam LinkStatus status) throws LinkException {
        LinkCollection response = new LinkCollection();
        response.setLinks(maintenanceService.getLinksByStatus(status));

        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Successfully Retrieved Link Data", response, null), HttpStatus.OK);
    }

    @PutMapping(value = "/maintenance/updateLink")
    public ResponseEntity<RestResponse<String>> updateLink(
            @RequestBody LinkUpdate request) throws LinkException {
        maintenanceService.updateLink(request);
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Successfully updated Link", null, null), HttpStatus.OK);
    }

    @PutMapping(value = "/maintenance/updateLinks")
    public ResponseEntity<RestResponse<String>> updateLinks(
            @RequestBody LinkUpdateRequest request) throws LinkException {
        maintenanceService.updateLinks(request);
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Successfully updated Link", null, null), HttpStatus.OK);
    }

    @DeleteMapping(value = "/maintenance/deleteLinkByUrl")
    public ResponseEntity<RestResponse<String>> deleteLink(
            @RequestParam String url) throws LinkException {
        maintenanceService.deleteLinkByUrl(url);

        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Successfully Deleted Link Data", url, null), HttpStatus.OK);
    }

    // Will use selenium to validate a link (resource intensive!!!)
    @PostMapping(value = "/maintenance/validateLink")
    public ResponseEntity<RestResponse<LinkBean>> postLink(@RequestParam String url) throws LinkException {
        LinkBean response = maintenanceService.validateLink(url);

        return new ResponseEntity<RestResponse<LinkBean>>(
                new RestResponse<LinkBean>(HttpStatus.OK.toString(), "Successfully Posted Link", response, null), HttpStatus.OK);
    }

    @PutMapping(value = "/maintenance/maintainLink")
    public ResponseEntity<RestResponse<LinkBean>> performLinkMaintenance(@RequestParam String url) throws LinkException {
        Link link = maintenanceService.performMaintenance(url);
        return new ResponseEntity<RestResponse<LinkBean>>(
                new RestResponse<LinkBean>(HttpStatus.OK.toString(), "Successfully performed maintenance}",convert(link), null), HttpStatus.OK);
    }

    @PutMapping(value = "/maintenance/performMaintenance")
    public ResponseEntity<RestResponse<LinkBean>> performLinkMaintenance() throws LinkException {
        maintenanceService.performMaintenance();
        return new ResponseEntity<RestResponse<LinkBean>>(
                new RestResponse<LinkBean>(HttpStatus.OK.toString(), "Successfully performed maintenance}",null, null), HttpStatus.OK);
    }
}

package com.koala.linkfilterapp.linkfilterapi.controller.maintenance;

import com.koala.linkfilterapp.linkfilterapi.api.common.dto.request.BanAction;
import com.koala.linkfilterapp.linkfilterapi.api.common.dto.response.RestResponse;
import com.koala.linkfilterapp.linkfilterapi.api.common.entity.IpAddress;
import com.koala.linkfilterapp.linkfilterapi.api.common.enums.IpSortType;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.LinkException;
import com.koala.linkfilterapp.linkfilterapi.service.common.impl.IpAddressServiceImpl;
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
public class IpMaintenanceController {
    Logger log = Logger.getLogger("IpMaintenanceController");

    @Autowired
    IpAddressServiceImpl ipAddressService;

    @PostMapping(value = "/maintenance/banIp")
    public ResponseEntity<RestResponse<List<IpAddress>>> banIpAddress(
            @RequestBody List<BanAction> request) throws LinkException {
        List<IpAddress> addresses = ipAddressService.manageIpBan(request);
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), String.format("Ips processed: %d", addresses.size()), addresses, null), HttpStatus.OK);
    }

    @GetMapping(value = "/maintenance/getAllIps")
    public ResponseEntity<RestResponse<List<IpAddress>>> getAllIps(HttpServletRequest request) throws LinkException {
        if (ipAddressService.checkIfBanned(request.getRemoteAddr())) {
            return null;
        }

        log.info(String.format("Request: %s", request.getAuthType()));
        List<IpAddress> res = ipAddressService.getAllIps();
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Ips fetched", res, null), HttpStatus.OK);
    }

    @GetMapping (value = "/maintenance/searchIps")
    public ResponseEntity<RestResponse<Page<IpAddress>>> searchIps(
            @RequestParam(required = false) String ipAddress,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "1") Integer size,
            @RequestParam IpSortType sortType, HttpServletRequest request) throws LinkException {
        if (ipAddressService.checkIfBanned(request.getRemoteAddr())) {
            return null;
        }

        log.info(String.format("Request: %s", request.getAuthType()));
        Page<IpAddress> res = ipAddressService.searchIps(page, size, sortType, ipAddress);
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Sponsors searched", res, null), HttpStatus.OK);
    }

}

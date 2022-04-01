package com.koala.linkfilterapp.linkfilterapi.controller.maintenance;

import com.koala.linkfilterapp.linkfilterapi.api.ipaddress.dto.BanAction;
import com.koala.linkfilterapp.linkfilterapi.api.common.dto.response.RestResponse;
import com.koala.linkfilterapp.linkfilterapi.api.ipaddress.entity.IpAddress;
import com.koala.linkfilterapp.linkfilterapi.api.common.enums.BanStatus;
import com.koala.linkfilterapp.linkfilterapi.api.common.enums.AddressType;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.CommonException;
import com.koala.linkfilterapp.linkfilterapi.api.ipaddress.dto.IpSearchBean;
import com.koala.linkfilterapp.linkfilterapi.api.ipaddress.enums.IpSortType;
import com.koala.linkfilterapp.linkfilterapi.service.ipaddress.impl.IpAddressServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.logging.Logger;

import static com.koala.linkfilterapp.linkfilterapi.controller.common.ControllerConstants.UI_SERVER_ORIGIN;

@CrossOrigin(origins = UI_SERVER_ORIGIN)
@RestController
public class IpMaintenanceController {
    Logger log = Logger.getLogger("IpMaintenanceController");

    @Autowired
    IpAddressServiceImpl ipAddressService;

    @PostMapping(value = "/maintenance/banIp")
    public ResponseEntity<RestResponse<List<IpAddress>>> banIpAddress(
            @RequestBody List<BanAction> request) throws CommonException {
        List<IpAddress> addresses = ipAddressService.manageIpBan(request);
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), String.format("Ips processed: %d", addresses.size()), addresses, null), HttpStatus.OK);
    }

    @GetMapping(value = "/maintenance/getAllIps")
    public ResponseEntity<RestResponse<List<IpAddress>>> getAllIps(HttpServletRequest request) throws CommonException {
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
            @RequestParam(required = false) BanStatus isBanned,
            @RequestParam(required = false) AddressType ipAddressType,
            @RequestParam(required = false) String lastAccessed,
            @RequestParam(required = false, defaultValue = "lastAccessed") IpSortType sortType,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection,
            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            HttpServletRequest request) throws CommonException {

        IpSearchBean searchBean = IpSearchBean.builder()
                .ipAddress(ipAddress).isBanned(isBanned).ipAddressType(ipAddressType).lastAccessed(lastAccessed).sortDirection(sortDirection)
                .sortType(sortType).pageNo(pageNo).pageSize(pageSize).build();

        log.info("Search request: " + searchBean);
        Page<IpAddress> res = ipAddressService.searchIps(searchBean);
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Sponsors searched", res, null), HttpStatus.OK);
    }

}

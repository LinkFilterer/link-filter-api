package com.koala.linkfilterapp.linkfilterapi.controller.maintenance;

import com.koala.linkfilterapp.linkfilterapi.api.common.dto.response.RestResponse;
import com.koala.linkfilterapp.linkfilterapi.api.common.enums.ReportType;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.LinkException;
import com.koala.linkfilterapp.linkfilterapi.api.report.dto.LinkReportBean;
import com.koala.linkfilterapp.linkfilterapi.service.report.maintenance.impl.ReportMaintenanceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

import static com.koala.linkfilterapp.linkfilterapi.controller.ControllerConstants.UI_SERVER_ORIGIN;

@CrossOrigin(origins = UI_SERVER_ORIGIN)
@RestController
public class ReportMaintenanceController {
    Logger log = Logger.getLogger("ReportMaintenanceController");

    @Autowired
    ReportMaintenanceServiceImpl service;

    @GetMapping(value = "/maintenance/getReportsByUrl")
    public ResponseEntity<RestResponse<List<LinkReportBean>>> getReportsByUrl(
            @RequestParam String url
    ) throws LinkException {
        List<LinkReportBean> response = service.getReportsByUrl(url);

        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Successfully retrieved reports", response, null), HttpStatus.OK);
    }

    @GetMapping(value = "/maintenance/getReportsByIp")
    public ResponseEntity<RestResponse<List<LinkReportBean>>> getReportsByIp(
            @RequestParam String ipAddress
    ) throws LinkException {

        List<LinkReportBean> response = service.getReportsByIp(ipAddress);

        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Successfully retrieved reports", response, null), HttpStatus.OK);
    }

    @GetMapping(value = "/maintenance/getReportsByUrlAndIp")
    public ResponseEntity<RestResponse<LinkReportBean>> getReportsByUrlAndIp(
            @RequestParam String url,
            @RequestParam String ipAddress
    ) throws LinkException {

        LinkReportBean response = service.getReportsByUrlAndIpAddress(url, ipAddress);

        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Successfully retrieved reports", response, null), HttpStatus.OK);
    }


    // Deletes all reports from given Ip
    @DeleteMapping(value = "/maintenance/deleteReportsByIp")
    public ResponseEntity<RestResponse<List<LinkReportBean>>> deleteReportsByIp(
            @RequestParam String ipAddress
    ) throws LinkException {
        List<LinkReportBean> response = service.deleteReportsByIp(ipAddress);
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Successfully deleted reports", response, null), HttpStatus.OK);

    }

}

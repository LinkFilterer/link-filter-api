package com.koala.linkfilterapp.linkfilterapi.controller.maintenance;

import com.koala.linkfilterapp.linkfilterapi.api.common.dto.response.RestResponse;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.CommonException;
import com.koala.linkfilterapp.linkfilterapi.api.report.dto.LinkReportBean;
import com.koala.linkfilterapp.linkfilterapi.api.report.dto.LinkReportSearchBean;
import com.koala.linkfilterapp.linkfilterapi.api.report.enums.LinkReportSearchField;
import com.koala.linkfilterapp.linkfilterapi.service.report.maintenance.impl.ReportMaintenanceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

import static com.koala.linkfilterapp.linkfilterapi.controller.common.ControllerConstants.UI_SERVER_ORIGIN;

@CrossOrigin(origins = UI_SERVER_ORIGIN)
@RestController
public class ReportMaintenanceController {
    Logger log = Logger.getLogger("ReportMaintenanceController");

    @Autowired
    ReportMaintenanceServiceImpl service;

    @GetMapping(value = "maintenance/getReports")
    public ResponseEntity<RestResponse<Page<LinkReportBean>>> getReports(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(name = "url", required = false) String url,
            @RequestParam(name = "validReport", required = false) Boolean validReport,
            @RequestParam(name = "ipAddress", required = false) String ipAddress,
            @RequestParam(name = "reportTime", required = false) String reportTime,
            @RequestParam(name = "sortType", required = false, defaultValue = "reportTime") LinkReportSearchField sortType,
            @RequestParam(name = "sortDirection", required = false, defaultValue = "asc") String sortDir,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") Integer pageNo) throws CommonException {

        LinkReportSearchBean searchBean = LinkReportSearchBean.builder()
                .id(id).url(url)
                .validReport(validReport).ipAddress(ipAddress)
                .reportTime(reportTime)
                .sortType(sortType)
                .sortDir(sortDir)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .build();

        Page<LinkReportBean> response = service.getReports(searchBean);

        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Successfully retrieved reports", response, null), HttpStatus.OK);
    }

    // Deletes all reports from given Ip
    @DeleteMapping(value = "/maintenance/deleteReportsByIp")
    public ResponseEntity<RestResponse<List<LinkReportBean>>> deleteReportsByIp(
            @RequestParam String ipAddress
    ) throws CommonException {
        List<LinkReportBean> response = service.deleteReportsByIp(ipAddress);
        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Successfully deleted reports", response, null), HttpStatus.OK);

    }

}

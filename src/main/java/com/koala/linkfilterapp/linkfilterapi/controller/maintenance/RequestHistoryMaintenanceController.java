package com.koala.linkfilterapp.linkfilterapi.controller.maintenance;

import com.koala.linkfilterapp.linkfilterapi.api.requesthistory.dto.RequestHistoryData;
import com.koala.linkfilterapp.linkfilterapi.api.requesthistory.dto.RequestHistorySearchBean;
import com.koala.linkfilterapp.linkfilterapi.api.requesthistory.dto.RequestHistoryStatResponse;
import com.koala.linkfilterapp.linkfilterapi.api.common.dto.response.RestResponse;
import com.koala.linkfilterapp.linkfilterapi.api.common.enums.TimeInterval;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.LinkException;
import com.koala.linkfilterapp.linkfilterapi.api.requesthistory.enums.RequestField;
import com.koala.linkfilterapp.linkfilterapi.api.requesthistory.enums.RequestType;
import com.koala.linkfilterapp.linkfilterapi.service.ipaddress.impl.RequestHistoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

import static com.koala.linkfilterapp.linkfilterapi.controller.ControllerConstants.UI_SERVER_MAINTENACE_ORIGIN;
import static com.koala.linkfilterapp.linkfilterapi.controller.ControllerConstants.UI_SERVER_ORIGIN;

@CrossOrigin(origins = {UI_SERVER_ORIGIN, UI_SERVER_MAINTENACE_ORIGIN})
@RestController
public class RequestHistoryMaintenanceController {
    Logger log = Logger.getLogger("RequestHistoryMaintenanceController");

    @Autowired
    RequestHistoryServiceImpl requestHistoryService;

    @GetMapping(value = "/maintenance/getAllRequestHistory")
    public ResponseEntity<RestResponse<Page<RequestHistoryData>>> getAllRequestHistory(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) RequestType requestType,
            @RequestParam(required = false) String url,
            @RequestParam(required = false) String requestedUrl,
            @RequestParam(required = false) String ipAddress,
            @RequestParam(required = false) String requestTime,
            @RequestParam(required = false, defaultValue = "asc") String sortDir,
            @RequestParam(required = false, defaultValue = "requestTime") RequestField sortType,
            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        RequestHistorySearchBean searchBean = RequestHistorySearchBean.builder()
                .id(id)
                .requestType(requestType)
                .url(url)
                .requestedUrl(requestedUrl)
                .ipAddress(ipAddress)
                .requestTime(requestTime)
                .sortDir(sortDir)
                .sortType(sortType)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .build();

        Page<RequestHistoryData> response = requestHistoryService.getAllRequestHistory(searchBean);

        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Successfully Retrieved History Data", response, null), HttpStatus.OK);
    }

    @GetMapping(value = "/maintenance/getRequestHistory")
    public ResponseEntity<RestResponse<RequestHistoryStatResponse>> getRequestHistory(
            @RequestParam (required = false) String url,
            @RequestParam TimeInterval timeInterval,
            @RequestParam String startingDate,
            @RequestParam String endDate) throws LinkException {
        RequestHistoryStatResponse response = requestHistoryService.getRequestHistoryStatistics(url, timeInterval, startingDate, endDate);

        return new ResponseEntity<>(
                new RestResponse<>(HttpStatus.OK.toString(), "Successfully Retrieved History Data", response, null), HttpStatus.OK);
    }
}

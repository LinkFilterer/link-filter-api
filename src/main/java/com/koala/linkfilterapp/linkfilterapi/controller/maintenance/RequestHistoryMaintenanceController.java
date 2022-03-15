package com.koala.linkfilterapp.linkfilterapi.controller.maintenance;

import com.koala.linkfilterapp.linkfilterapi.api.common.dto.response.RequestHistoryData;
import com.koala.linkfilterapp.linkfilterapi.api.common.dto.response.RequestHistoryStatResponse;
import com.koala.linkfilterapp.linkfilterapi.api.common.dto.response.RestResponse;
import com.koala.linkfilterapp.linkfilterapi.api.common.entity.RequestHistory;
import com.koala.linkfilterapp.linkfilterapi.api.common.enums.TimeInterval;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.LinkException;
import com.koala.linkfilterapp.linkfilterapi.service.common.impl.RequestHistoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
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
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam(defaultValue = "requestTime", required = false) String sortType) {

        Page<RequestHistoryData> response = requestHistoryService.getAllRequestHistory(page, size, sortType);

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

package com.koala.linkfilterapp.linkfilterapi.api.requesthistory.dto;

import com.koala.linkfilterapp.linkfilterapi.api.common.enums.TimeInterval;
import lombok.Data;

import java.util.List;

@Data
public class RequestHistoryStatResponse {
    TimeInterval interval;
    List<RequestHistoryStatistic> requestHistoryStatistics;
}

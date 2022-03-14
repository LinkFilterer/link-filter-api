package com.koala.linkfilterapp.linkfilterapi.api.common.dto.response;

import com.koala.linkfilterapp.linkfilterapi.api.common.enums.TimeInterval;
import lombok.Data;

import java.util.List;

@Data
public class RequestHistoryStatResponse {
    TimeInterval interval;
    List<RequestHistoryStatistic> requestHistoryStatistics;
}

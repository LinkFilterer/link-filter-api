package com.koala.linkfilterapp.linkfilterapi.api.requesthistory.dto;

import com.koala.linkfilterapp.linkfilterapi.api.requesthistory.enums.RequestField;
import com.koala.linkfilterapp.linkfilterapi.api.requesthistory.enums.RequestType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestHistorySearchBean {
    Integer id;
    RequestType requestType;
    String url;
    String requestedUrl;
    String ipAddress;
    String requestTime;
    String sortDir;
    RequestField sortType;
    Integer pageSize;
    Integer pageNo;
}

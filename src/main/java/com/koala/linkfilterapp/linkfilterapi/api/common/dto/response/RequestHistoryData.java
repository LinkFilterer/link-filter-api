package com.koala.linkfilterapp.linkfilterapi.api.common.dto.response;

import com.koala.linkfilterapp.linkfilterapi.api.common.enums.RequestType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RequestHistoryData implements Serializable {
    Integer id;
    RequestType requestType;
    String url;
    String requestedUrl;
    String ipAddress;
    Date requestTime;
}

package com.koala.linkfilterapp.linkfilterapi.api.requesthistory.dto;

import com.koala.linkfilterapp.linkfilterapi.api.requesthistory.enums.RequestType;
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

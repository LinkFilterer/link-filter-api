package com.koala.linkfilterapp.linkfilterapi.api.report.dto;

import com.koala.linkfilterapp.linkfilterapi.api.link.dto.response.LinkBean;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class LinkReportBean implements Serializable {
    Integer id;

    String url;

    String reportType;

    String ipAddress;

    Date reportTime;

    LinkBean linkRequested;
}

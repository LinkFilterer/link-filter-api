package com.koala.linkfilterapp.linkfilterapi.api.report.dto;

import com.koala.linkfilterapp.linkfilterapi.api.report.enums.LinkReportSearchField;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LinkReportSearchBean {
    String id;
    String url;
    Boolean validReport;
    String ipAddress;
    String reportTime;
    LinkReportSearchField sortType;
    String sortDir;
    Integer pageSize;
    Integer pageNo;
}

package com.koala.linkfilterapp.linkfilterapi.api.link.dto.request;

import com.koala.linkfilterapp.linkfilterapi.api.link.enums.LinkSortType;
import com.koala.linkfilterapp.linkfilterapi.api.link.enums.LinkStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LinkSearchBean {
    String url;
    LinkStatus status;
    String securityLevel;
    String badCount;
    String creationDate;
    String modifiedDate;
    String description;
    Boolean isConnectable;
    Integer statusCode;
    String lastMaintenance;
    String whoIsDate;
    LinkSortType sortType;
    String sortDir;
    Integer pageNo;
    Integer pageSize;
}

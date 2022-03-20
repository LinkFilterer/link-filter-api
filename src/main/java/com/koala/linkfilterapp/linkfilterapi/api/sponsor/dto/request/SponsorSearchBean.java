package com.koala.linkfilterapp.linkfilterapi.api.sponsor.dto.request;

import com.koala.linkfilterapp.linkfilterapi.api.sponsor.enums.SponsorSortType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SponsorSearchBean {
    String id;
    String description;
    String banner;
    String url;
    String creationDate;
    String endDate;
    Boolean isExpired;
    Double weight;
    SponsorSortType sortType;
    String sortDir;
    Integer pageSize;
    Integer pageNo;
}

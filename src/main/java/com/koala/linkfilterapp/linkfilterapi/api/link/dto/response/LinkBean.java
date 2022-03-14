package com.koala.linkfilterapp.linkfilterapi.api.link.dto.response;

import com.koala.linkfilterapp.linkfilterapi.api.sponsor.dto.response.SponsorBean;
import com.koala.linkfilterapp.linkfilterapi.api.common.enums.LinkStatus;
import lombok.Data;

@Data
public class LinkBean {
    String url;
    LinkStatus status;
    String description;
    Integer rating;
    SponsorBean sponsor;
}

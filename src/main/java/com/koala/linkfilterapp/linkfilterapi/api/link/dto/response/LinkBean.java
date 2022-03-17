package com.koala.linkfilterapp.linkfilterapi.api.link.dto.response;

import com.koala.linkfilterapp.linkfilterapi.api.link.enums.LinkStatus;
import com.koala.linkfilterapp.linkfilterapi.api.sponsor.dto.response.SponsorBean;
import lombok.Data;

@Data
public class LinkBean {
    String url;
    LinkStatus status;
    String description;
    Integer rating;
    SponsorBean sponsor;
}

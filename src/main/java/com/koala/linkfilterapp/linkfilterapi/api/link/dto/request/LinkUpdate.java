package com.koala.linkfilterapp.linkfilterapi.api.link.dto.request;

import com.koala.linkfilterapp.linkfilterapi.api.link.enums.LinkStatus;
import lombok.Data;

@Data
public class LinkUpdate {
    String url;
    String description;
    Integer securityLevel;
    LinkStatus status;
    boolean approve;
}

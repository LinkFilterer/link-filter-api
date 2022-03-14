package com.koala.linkfilterapp.linkfilterapi.api.link.dto.request;

import com.koala.linkfilterapp.linkfilterapi.api.common.enums.LinkStatus;
import lombok.Data;

import java.io.Serializable;

@Data
public class CreateLinkRequest implements Serializable {
    String url;

    LinkStatus status;

    Integer securityLevel;

    String description;
}

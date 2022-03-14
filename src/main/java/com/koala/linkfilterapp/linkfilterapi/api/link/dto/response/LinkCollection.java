package com.koala.linkfilterapp.linkfilterapi.api.link.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class LinkCollection {
    List<LinkBean> links;
}

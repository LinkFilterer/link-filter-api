package com.koala.linkfilterapp.linkfilterapi.api.link.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class LinkUpdateRequest {
    List<LinkUpdate> approvalList;
}

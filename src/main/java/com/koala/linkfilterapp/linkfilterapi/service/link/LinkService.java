package com.koala.linkfilterapp.linkfilterapi.service.link;

import com.koala.linkfilterapp.linkfilterapi.api.link.dto.response.LinkBean;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.LinkException;

public interface LinkService {
    LinkBean checkLink(String url, String ipAddress) throws LinkException;
}

package com.koala.linkfilterapp.linkfilterapi.service.link;

import com.koala.linkfilterapp.linkfilterapi.api.common.exception.CommonException;
import com.koala.linkfilterapp.linkfilterapi.api.link.dto.response.LinkBean;
import com.koala.linkfilterapp.linkfilterapi.api.requesthistory.entity.RequestHistory;

public interface LinkService {
    LinkBean checkLink(String url, String ipAddress, RequestHistory requestHistory) throws CommonException;
}

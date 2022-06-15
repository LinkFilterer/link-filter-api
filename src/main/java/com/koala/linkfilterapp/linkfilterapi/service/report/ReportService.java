package com.koala.linkfilterapp.linkfilterapi.service.report;

import com.koala.linkfilterapp.linkfilterapi.api.common.exception.CommonException;
import com.koala.linkfilterapp.linkfilterapi.api.link.entity.Link;

public interface ReportService {
    Link reportLink(String url, String ipAddress, String userId, boolean isValid) throws CommonException;
}

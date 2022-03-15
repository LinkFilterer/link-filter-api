package com.koala.linkfilterapp.linkfilterapi.service.report;

import com.koala.linkfilterapp.linkfilterapi.api.link.entity.Link;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.LinkException;

public interface ReportService {
    Link reportLink(String url, String ipAddress, boolean isValid) throws LinkException;
}

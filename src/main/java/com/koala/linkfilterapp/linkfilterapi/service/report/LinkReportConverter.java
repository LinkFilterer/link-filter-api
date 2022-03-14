package com.koala.linkfilterapp.linkfilterapi.service.report;

import com.koala.linkfilterapp.linkfilterapi.api.link.entity.Link;
import com.koala.linkfilterapp.linkfilterapi.api.report.dto.LinkReportBean;
import com.koala.linkfilterapp.linkfilterapi.api.report.entity.LinkReport;
import com.koala.linkfilterapp.linkfilterapi.service.link.LinkConverter;

import java.sql.Timestamp;

public class LinkReportConverter {
    public static LinkReport convert(Link link) {
        LinkReport report = new LinkReport();
        report.setUrl(link.getUrl());
        report.setReportTime(new Timestamp(System.currentTimeMillis()));
        return report;
    }

    public static LinkReportBean convert(LinkReport entity) {
        LinkReportBean bean = new LinkReportBean();
        bean.setId(entity.getId());
        bean.setUrl(entity.getUrl());
        bean.setReportType(entity.getValidReport() ? "VALID" : "INVALID");
        bean.setIpAddress(entity.getIpAddress());
        bean.setReportTime(entity.getReportTime());
        bean.setLinkRequested(LinkConverter.convert(entity.getLinkRequested()));
        return bean;
    }
}

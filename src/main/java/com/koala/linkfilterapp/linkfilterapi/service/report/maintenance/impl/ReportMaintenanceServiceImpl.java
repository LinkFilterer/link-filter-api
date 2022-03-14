package com.koala.linkfilterapp.linkfilterapi.service.report.maintenance.impl;

import com.koala.linkfilterapp.linkfilterapi.api.common.exception.LinkException;
import com.koala.linkfilterapp.linkfilterapi.api.report.dto.LinkReportBean;
import com.koala.linkfilterapp.linkfilterapi.api.report.entity.LinkReport;
import com.koala.linkfilterapp.linkfilterapi.repository.LinkReportRepository;
import com.koala.linkfilterapp.linkfilterapi.service.report.LinkReportConverter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ReportMaintenanceServiceImpl {
    Logger log = Logger.getLogger("ReportMaintenanceService");

    @Autowired
    LinkReportRepository repository;

    public List<LinkReportBean> getReportsByUrl(String url) throws LinkException {
        if (Strings.isBlank(url)) {
            LinkException exception = new LinkException(HttpStatus.BAD_REQUEST, "Invalid url value", null, "Get Report", null);
            log.warning(exception.toString());
            throw exception;
        }
        return repository.findByUrl(url).stream().map(LinkReportConverter::convert).collect(Collectors.toList());
    }

    public LinkReportBean getReportsByUrlAndIpAddress(String url, String ipAddress) throws LinkException {
        if (Strings.isBlank(url)) {
            LinkException exception = new LinkException(HttpStatus.BAD_REQUEST, "Invalid url value", null, "Get Report", null);
            log.warning(exception.toString());
            throw exception;
        }
        Optional<LinkReport> foundReport = repository.findByUrlAndIpAddress(url, ipAddress);
        return foundReport.map(LinkReportConverter::convert).orElseGet(LinkReportBean::new);

    }
    public List<LinkReportBean> getReportsByIp(String ip) throws LinkException {
        if (Strings.isBlank(ip)) {
            LinkException exception = new LinkException(HttpStatus.BAD_REQUEST, "Invalid ip value", null, "Get Report", null);
            log.warning(exception.toString());
            throw exception;
        }
        return repository.findByIpAddress(ip).stream().map(LinkReportConverter::convert).collect(Collectors.toList());

    }


    public List<LinkReportBean> deleteReportsByIp(String ip) throws LinkException {
        List<LinkReport> reports = repository.findByIpAddress(ip);
        if (reports.isEmpty()) {
            LinkException exception = new LinkException(HttpStatus.NOT_FOUND, "No reports found from ip " + ip , null, "Delete Report", null);
            log.warning(exception.toString());
            throw exception;
        }
        repository.deleteAll(reports);
        return reports.stream().map(LinkReportConverter::convert).collect(Collectors.toList());
    }

}

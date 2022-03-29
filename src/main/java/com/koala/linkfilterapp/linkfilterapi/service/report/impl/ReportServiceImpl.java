package com.koala.linkfilterapp.linkfilterapi.service.report.impl;

import com.koala.linkfilterapp.linkfilterapi.api.link.enums.LinkStatus;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.CommonException;
import com.koala.linkfilterapp.linkfilterapi.api.link.entity.Link;
import com.koala.linkfilterapp.linkfilterapi.api.report.entity.LinkReport;
import com.koala.linkfilterapp.linkfilterapi.repository.LinkReportRepository;
import com.koala.linkfilterapp.linkfilterapi.repository.LinkRepository;
import com.koala.linkfilterapp.linkfilterapi.service.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.logging.Logger;

import static com.koala.linkfilterapp.linkfilterapi.service.common.CommonApiConstants.REPORT_INSPECTION_THRESHOLD;
import static com.koala.linkfilterapp.linkfilterapi.service.report.LinkReportConverter.convert;

@Service
public class ReportServiceImpl implements ReportService {
    Logger log = Logger.getLogger("ReportServiceImpl");

    @Autowired
    LinkRepository linkRepository;

    @Autowired
    LinkReportRepository reportRepository;

    public Link reportLink(String url, String ipAddress, boolean isValid) throws CommonException {
        Optional<LinkReport> foundReport = reportRepository.findByUrlAndIpAddress(url, ipAddress);
        // TODO: Rework to do interval-based reported per ip
        if (foundReport.isPresent()) {
            foundReport.get().setValidReport(isValid);
            reportRepository.save(foundReport.get());
            return saveUpdateReport(foundReport.get(), isValid);
        } else {
            log.info(String.format("No report present for %s", url));
            return saveNewReport(url, ipAddress, isValid);
        }
    }

    private Link saveNewReport(String url, String ipAddress, boolean isValid) {
        Optional<Link> foundLink = linkRepository.findById(url);
        if (foundLink.isPresent() && foundLink.get().getStatus().equals(LinkStatus.VERIFIED)) {
            log.info(String.format("Link already verified %s", url));
            return foundLink.get();
        } else if (foundLink.isPresent()) {
            log.info(String.format("Updating previous report: %s", url));
            LinkReport linkReport = convert(foundLink.get());
            linkReport.setIpAddress(ipAddress);
            linkReport.setValidReport(isValid);
            linkReport.setLinkRequested(foundLink.get());
            reportRepository.save(linkReport);
            return saveUpdateReport(linkReport, isValid);
        } else {
            log.info(String.format("Reporting new Link: %s", url));
            Link link = new Link();
            link.setStatus(LinkStatus.SUSPICIOUS);
            link.setUrl(url);
            link.setCreationDate(new Timestamp(System.currentTimeMillis()));
            linkRepository.save(link);
            LinkReport linkReport = convert(link);
            linkReport.setLinkRequested(link);
            linkReport.setIpAddress(ipAddress);
            linkReport.setValidReport(isValid);
            linkReport.setLinkRequested(link);
            reportRepository.save(linkReport);
            return saveUpdateReport(linkReport, isValid);
        }
    }

    private Link saveUpdateReport(LinkReport report, boolean isValid) {
        Optional<Link> foundLink = linkRepository.findById(report.getUrl());
        if (foundLink.isPresent() && foundLink.get().getStatus().equals(LinkStatus.VERIFIED)) {
            return null;
        }
        long validReportCnt = reportRepository.countByUrlAndValidReport(report.getUrl(), true);
        long invalidReportCnt = reportRepository.countByUrlAndValidReport(report.getUrl(), false);
        log.info(String.format("Invalid count: %d Valid Count: %d", invalidReportCnt, validReportCnt));
        long totalReports = validReportCnt + invalidReportCnt;
        // TODO if overwriting a previous report, correct count logic
        LinkStatus status = null;
        int rating = 0;
        if (totalReports == 1 || totalReports == 0) {
            status = LinkStatus.SUSPICIOUS;
            rating = ((int) (validReportCnt - invalidReportCnt));
        } else if (totalReports >= REPORT_INSPECTION_THRESHOLD) {
            status = LinkStatus.NEED_INSPECTION;
        } else {
            rating = calculateRating(validReportCnt, invalidReportCnt);
            if (rating >= -8 && rating < -4) { // Between -8 to -4 is invalid
                status = LinkStatus.INVALID;
            } else if (rating >= -4 && rating < 4) { // Between -4 to 4 is suspicious
                status = LinkStatus.SUSPICIOUS;
            } else { // if >= 4 is valid
                status = LinkStatus.VALID;
            }
        }
        log.info(String.format("Calulated Rating: %d Status: %s for %s", rating, status, report.getUrl()));
        report.setValidReport(isValid);
        report.getLinkRequested().setSecurityLevel(rating);
        report.getLinkRequested().setStatus(status);
        report.setReportTime(new Timestamp(System.currentTimeMillis()));
        reportRepository.save(report);
        return report.getLinkRequested();
    }

    // Determines Link's rating depending on valid/invalid count
    // scale of -10 to 10
    // - 9, -10, 9, 10 are considered officially validated and are manually;
    private int calculateRating(long validCnt, long invalidCnt) {
        return (int) Math.floor((double) validCnt / (double) (invalidCnt + validCnt) * 16) - 8;
    }
}

package com.koala.linkfilterapp.linkfilterapi.service.report.maintenance.impl;

import com.koala.linkfilterapp.linkfilterapi.api.common.exception.LinkException;
import com.koala.linkfilterapp.linkfilterapi.api.report.dto.LinkReportBean;
import com.koala.linkfilterapp.linkfilterapi.api.report.dto.LinkReportSearchBean;
import com.koala.linkfilterapp.linkfilterapi.api.report.entity.LinkReport;
import com.koala.linkfilterapp.linkfilterapi.repository.LinkReportRepository;
import com.koala.linkfilterapp.linkfilterapi.service.report.LinkReportConverter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.persistence.criteria.Predicate;

import static java.util.Objects.nonNull;

@Service
public class ReportMaintenanceServiceImpl {
    Logger log = Logger.getLogger("ReportMaintenanceService");

    @Autowired
    LinkReportRepository repository;


    public Page<LinkReportBean> getReports(LinkReportSearchBean searchBean) {
        Specification<LinkReport> querySpec = createQuery(searchBean);
        Pageable pageRequest = getPageableRequest(searchBean);
        return repository.findAll(querySpec, pageRequest).map(LinkReportConverter::convert);
    }

    private Pageable getPageableRequest(LinkReportSearchBean searchBean) {
        Sort sortOrder = Sort.by(searchBean.getSortType().toString());
        Optional<Sort.Direction> sortDirection = Sort.Direction.fromOptionalString(searchBean.getSortDir());

        if (sortDirection.isPresent()) {
            sortOrder = Sort.by(sortDirection.get(), searchBean.getSortType().toString());
        }

        return PageRequest.of(searchBean.getPageNo(), searchBean.getPageSize(), sortOrder);
    }

    private Specification<LinkReport> createQuery(LinkReportSearchBean searchBean) {
        return (root, query, builder) -> {
            final List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.hasText(searchBean.getId())) {
                predicates.add(builder.equal(root.<String>get("id"), searchBean.getId()));
            }
            if(StringUtils.hasText(searchBean.getReportTime())) {
                predicates.add(builder.equal(root.<String>get("reportTime"), searchBean.getReportTime()));
            }
            if(StringUtils.hasText(searchBean.getIpAddress())) {
                predicates.add(builder.equal(root.<String>get("ipAddress"), searchBean.getIpAddress()));
            }
            if(StringUtils.hasText(searchBean.getUrl())) {
                predicates.add(builder.equal(root.<String>get("url"), searchBean.getUrl()));
            }
            if(nonNull(searchBean.getValidReport())) {
                predicates.add(builder.equal(root.<String>get("validReport"), searchBean.getValidReport()));
            }

            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }


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

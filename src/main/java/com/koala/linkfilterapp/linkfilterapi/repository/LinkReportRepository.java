package com.koala.linkfilterapp.linkfilterapi.repository;

import com.koala.linkfilterapp.linkfilterapi.api.report.entity.LinkReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LinkReportRepository extends JpaRepository<LinkReport, String>, JpaSpecificationExecutor<LinkReport> {
    Optional<LinkReport> findByUrlAndIpAddress(String url, String ipAddress);

    List<LinkReport> findByUrl(String url);

    List<LinkReport> findByIpAddress(String ipAddress);

    long countByUrlAndValidReport(String url, boolean valid);
}

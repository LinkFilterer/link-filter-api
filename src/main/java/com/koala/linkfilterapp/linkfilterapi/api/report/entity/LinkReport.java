package com.koala.linkfilterapp.linkfilterapi.api.report.entity;

import com.koala.linkfilterapp.linkfilterapi.api.link.entity.Link;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "LINK_REPORTS")
public class LinkReport {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    Integer id;

    @Column(name = "url")
    String url;

    @Column(name = "userId")
    String userId;

    // Report Exclusive Fields below

    @Column(name = "reportType", nullable = false)
    Boolean validReport = false;

    @Column(name = "ipAddress", nullable = false, updatable = false)
    String ipAddress;

    @Column (name = "requestTime", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    Date reportTime;

    @JoinColumn(name = "url", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    Link linkRequested;
}

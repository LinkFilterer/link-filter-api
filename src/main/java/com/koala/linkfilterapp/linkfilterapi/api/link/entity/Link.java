package com.koala.linkfilterapp.linkfilterapi.api.link.entity;

import com.koala.linkfilterapp.linkfilterapi.api.common.entity.RequestHistory;
import com.koala.linkfilterapp.linkfilterapi.api.link.enums.LinkStatus;
import com.koala.linkfilterapp.linkfilterapi.api.report.entity.LinkReport;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "link")
public class Link implements Serializable {
    @Id
    @Column (name = "url")
    String url;

    @Column (name = "status")
    LinkStatus status;

    @Column (name = "securityLevel")
    Integer securityLevel;

    @Column (name = "badCount")
    Integer badCount;

    @Column (name = "creationDate")
    @Temporal(TemporalType.DATE)
    Date creationDate;

    @Column (name = "modifiedDate")
    @Temporal(TemporalType.DATE)
    Date modifiedDate;

    @Column (name = "description")
    String description;

    @Column (name = "isConnectable")
    Boolean isConnectable;

    @Column (name = "statusCode")
    Integer statusCode;

    @Column (name = "lastMaintenance")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastMaintenance;

    @Column (name = "whoIsDate")
    @Temporal(TemporalType.TIMESTAMP)
    Date whoIsDate;

    @ToString.Exclude
    @OneToMany (orphanRemoval = true)
    @JoinColumn (name = "url")
    List<RequestHistory> requestHistory;

    @ToString.Exclude
    @OneToMany (orphanRemoval = true)
    @JoinColumn (name = "url")
    List<LinkReport> reports;

    public void incrementBadCount() {
        this.badCount++;
    }
}

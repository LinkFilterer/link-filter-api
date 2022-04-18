package com.koala.linkfilterapp.linkfilterapi.api.requesthistory.entity;

import com.koala.linkfilterapp.linkfilterapi.api.common.enums.AddressType;
import com.koala.linkfilterapp.linkfilterapi.api.link.entity.Link;
import com.koala.linkfilterapp.linkfilterapi.api.requesthistory.enums.RequestType;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Immutable;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "REQUEST_HISTORY")
@Immutable
@Data
public class RequestHistory {
    @Id
    @GeneratedValue (strategy=GenerationType.IDENTITY)
    Integer id;

    @Column (name = "requestType")
    RequestType requestType;

    @Column (name = "url")
    String url;

    // History Exclusive Fields below

    @Column (name = "requestedUrl")
    String requestedUrl;

    @Column(name = "ipAddress", nullable = false, updatable = false)
    String ipAddress;

    @Column(name = "source")
    AddressType source;

    @Column(name = "userId")
    String userId;

    @Column (name = "requestTime")
    @Temporal(TemporalType.TIMESTAMP)
    Date requestTime;

    @ToString.Exclude
    @JoinColumn(name = "url", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    Link linkRequested;
}

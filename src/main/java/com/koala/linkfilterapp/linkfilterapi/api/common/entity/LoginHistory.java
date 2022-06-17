package com.koala.linkfilterapp.linkfilterapi.api.common.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class LoginHistory {
    @Id
    @Column(name = "ip", length = 39)
    String ip;

    @Column(name = "name", length = 32)
    String name;

    boolean isAuthenticated;

    Long accessCount;

    Long successfulLogins;

    @Temporal(TemporalType.TIMESTAMP)
    Date initialAccessDate;

    @Temporal(TemporalType.TIMESTAMP)
    Date latestAccessDate;
}

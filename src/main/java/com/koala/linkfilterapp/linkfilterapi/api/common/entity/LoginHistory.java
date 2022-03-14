package com.koala.linkfilterapp.linkfilterapi.api.common.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@Entity
public class LoginHistory {
    @Id
    String ip;

    String name;

    boolean isAuthenticated;

    Long accessCount;

    Long successfulLogins;

    @Temporal(TemporalType.TIMESTAMP)
    Date initialAccessDate;

    @Temporal(TemporalType.TIMESTAMP)
    Date latestAccessDate;
}

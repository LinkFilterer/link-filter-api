package com.koala.linkfilterapp.linkfilterapi.api.sponsor.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "sponsor")
public class Sponsor {
    @Id
    @Column (name = "projectName", length = 64)
    String id;

    @Column (name = "description")
    String description;

    @Column (name = "banner")
    String banner;

    @Column (name = "url", length = 64)
    String url;

    @Column (name = "creationDate")
    @Temporal(TemporalType.DATE)
    Date creationDate;

    @Column (name = "endDate")
    @Temporal(TemporalType.DATE)
    Date endDate;

    @Column (name = "weight", nullable = false)
    Double weight = 1.0;
}

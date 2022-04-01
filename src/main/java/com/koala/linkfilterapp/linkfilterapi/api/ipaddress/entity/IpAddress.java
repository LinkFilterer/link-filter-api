package com.koala.linkfilterapp.linkfilterapi.api.ipaddress.entity;

import com.koala.linkfilterapp.linkfilterapi.api.common.enums.BanStatus;
import com.koala.linkfilterapp.linkfilterapi.api.common.enums.AddressType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Table (name = "ip_table")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IpAddress {
    @EmbeddedId
    IpAddressPk id;

    @Column(name = "isBanned")
    BanStatus isBanned;

    @Column(name = "addressType")
    AddressType addressType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lastAccessed")
    Date lastAccessed;
}

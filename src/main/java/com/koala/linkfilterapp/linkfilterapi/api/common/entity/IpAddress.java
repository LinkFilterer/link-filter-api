package com.koala.linkfilterapp.linkfilterapi.api.common.entity;

import com.koala.linkfilterapp.linkfilterapi.api.common.enums.BanStatus;
import com.koala.linkfilterapp.linkfilterapi.api.common.enums.IpAddressType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table (name = "ip_table")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IpAddress {
    @Id
    String ipAddress;

    @Column(name = "isBanned")
    BanStatus isBanned;

    @Column(name = "addressType")
    IpAddressType addressType;
}

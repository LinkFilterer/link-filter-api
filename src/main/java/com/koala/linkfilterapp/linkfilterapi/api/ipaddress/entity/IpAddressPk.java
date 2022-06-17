package com.koala.linkfilterapp.linkfilterapi.api.ipaddress.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class IpAddressPk implements Serializable {
    @Column(name = "ipAddress", length = 39)
    String ipAddress;

    @Column(name = "userId", length = 32)
    String userId;
}

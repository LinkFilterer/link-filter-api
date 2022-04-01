package com.koala.linkfilterapp.linkfilterapi.api.ipaddress.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class IpAddressPk implements Serializable {
    String ipAddress;
    String userId;
}

package com.koala.linkfilterapp.linkfilterapi.api.ipaddress.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class IpBanRequest implements Serializable {
    List<BanAction> requests;

}

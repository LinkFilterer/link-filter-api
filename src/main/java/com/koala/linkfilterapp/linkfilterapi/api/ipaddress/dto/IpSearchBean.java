package com.koala.linkfilterapp.linkfilterapi.api.ipaddress.dto;

import com.koala.linkfilterapp.linkfilterapi.api.common.enums.BanStatus;
import com.koala.linkfilterapp.linkfilterapi.api.common.enums.IpAddressType;
import com.koala.linkfilterapp.linkfilterapi.api.ipaddress.enums.IpSortType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IpSearchBean {
    String ipAddress;
    BanStatus isBanned;
    IpAddressType ipAddressType;
    String lastAccessed;
    IpSortType sortType;
    String sortDirection;
    Integer pageNo;
    Integer pageSize;
}

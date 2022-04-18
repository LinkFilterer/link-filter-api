package com.koala.linkfilterapp.linkfilterapi.service.ipaddress;

import com.koala.linkfilterapp.linkfilterapi.api.common.enums.AddressType;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.CommonException;
import com.koala.linkfilterapp.linkfilterapi.api.ipaddress.dto.BanAction;
import com.koala.linkfilterapp.linkfilterapi.api.ipaddress.entity.IpAddress;

import java.util.List;

public interface IpAddressService {
    boolean checkIfBanned(String ipAddress, AddressType addressType);

    List<IpAddress> manageIpBan(List<BanAction> request) throws CommonException;
}

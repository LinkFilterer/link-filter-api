package com.koala.linkfilterapp.linkfilterapi.service.common;

import com.koala.linkfilterapp.linkfilterapi.api.common.dto.request.BanAction;
import com.koala.linkfilterapp.linkfilterapi.api.common.entity.IpAddress;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.LinkException;

import java.util.List;

public interface IpAddressService {
    boolean checkIfBanned(String ipAddress);

    List<IpAddress> manageIpBan(List<BanAction> request) throws LinkException;
}

package com.koala.linkfilterapp.linkfilterapi.api.common.enums;

public enum IpSortType {
    ipAddress("ipAddress"),
    isBanned("isBanned"),
    addressType("addressType");

    private final String sortType;

    private IpSortType(String sortType) {
        this.sortType = sortType;
    }
}

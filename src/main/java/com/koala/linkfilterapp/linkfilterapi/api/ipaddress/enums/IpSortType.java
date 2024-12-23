package com.koala.linkfilterapp.linkfilterapi.api.ipaddress.enums;

public enum IpSortType {
    ipAddress("ipAddress"), isBanned("isBanned"), addressType("addressType"), lastAccessed("lastAccessed");

    private final String fieldName;

    IpSortType(String fieldName) {
        this.fieldName = fieldName;
    }
}

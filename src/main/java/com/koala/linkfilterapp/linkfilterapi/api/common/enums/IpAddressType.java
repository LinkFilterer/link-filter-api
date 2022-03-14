package com.koala.linkfilterapp.linkfilterapi.api.common.enums;

public enum IpAddressType {
    WEB("WEB"),
    DISCORD("DISCORD"),
    TELEGRAM("TELEGRAM");

    private final String source;

    private IpAddressType(String source) {
        this.source = source;
    }
}

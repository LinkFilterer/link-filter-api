package com.koala.linkfilterapp.linkfilterapi.api.common.enums;

public enum AddressType {
    WEB("WEB"),
    DISCORD("DISCORD"),
    TELEGRAM("TELEGRAM"),
    ADMIN("ADMIN"),

    UNKNOWN("UNKNOWN");

    private final String source;

    private AddressType(String source) {
        this.source = source;
    }

}

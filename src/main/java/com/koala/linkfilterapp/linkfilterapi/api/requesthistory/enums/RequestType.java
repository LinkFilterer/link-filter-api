package com.koala.linkfilterapp.linkfilterapi.api.requesthistory.enums;

public enum RequestType {
    CHECK("CHECK"), REPORT("REPORT");

    private final String type;

    private RequestType(String type) {
        this.type = type;
    }
}

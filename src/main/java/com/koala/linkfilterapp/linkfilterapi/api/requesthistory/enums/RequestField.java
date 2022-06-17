package com.koala.linkfilterapp.linkfilterapi.api.requesthistory.enums;

public enum RequestField {
    id("id"), requestType("requestType"), url("url"), requestedUrl("requestedUrl"),
    ipAddress("ipAddress"), requestTime("requestTime");

    private final String requestField;

    RequestField(String requestField) {
        this.requestField = requestField;
    }
}

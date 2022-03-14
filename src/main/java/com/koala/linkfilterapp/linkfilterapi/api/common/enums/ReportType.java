package com.koala.linkfilterapp.linkfilterapi.api.common.enums;

public enum ReportType {
    VALID("VALID"), INVALID("INVALID");

    private final String reportType;

    private ReportType(String reportType) {
        this.reportType = reportType;
    }
}

package com.koala.linkfilterapp.linkfilterapi.api.report.enums;

public enum ReportType {
    VALID("VALID"), INVALID("INVALID");

    private final String reportType;

    ReportType(String reportType) {
        this.reportType = reportType;
    }
}

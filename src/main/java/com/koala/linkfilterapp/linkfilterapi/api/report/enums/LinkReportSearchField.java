package com.koala.linkfilterapp.linkfilterapi.api.report.enums;

public enum LinkReportSearchField {
    id("id"), url("url"), validReport("validReport"), ipAddress("ipAddress"),
    reportTime("reportTime");

    private final String fieldName;

    LinkReportSearchField(String fieldName) {
        this.fieldName = fieldName;
    }

/*
*
*     @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Integer id;

    @Column(name = "url")
    String url;

    // Report Exclusive Fields below

    @Column(name = "reportType", nullable = false)
    Boolean validReport = false;

    @Column(name = "ipAddress", nullable = false, updatable = false)
    String ipAddress;

    @Column (name = "requestTime", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    Date reportTime;
* */

}

package com.koala.linkfilterapp.linkfilterapi.api.common.enums;

public enum TimeInterval {
    SECOND("SECOND"),
    MINUTE("MINUTE"),
    HOUR("HOUR"),
    DAY("DAY"),
    MONTH("MONTH"),
    YEAR("YEAR");

    private final String interval;

    TimeInterval(String interval) {
        this.interval = interval;
    }
}

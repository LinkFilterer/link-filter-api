package com.koala.linkfilterapp.linkfilterapi.api.link.enums;

public enum LinkStatus {
    VERIFIED("VERIFIED"), // 0
    VALID("VALID"), // 1
    SUSPICIOUS("SUSPICIOUS"), // 2
    INVALID("INVALID"), // 3
    BLACKLISTED("BLACKLISTED"), // 4
    NEED_INSPECTION("NEED_INSPECTION"), // 5
    NOT_CONNECTABLE("NOT_CONNECTABLE"), // 6
    UNPROCCESSED("UNPROCESSED"), // 7
    UNKNOWN("UNKNOWN"); // 8

    private final String status;

    LinkStatus(String status) {
        this.status = status;
    }
}

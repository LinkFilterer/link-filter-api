package com.koala.linkfilterapp.linkfilterapi.api.common.enums;

public enum BanStatus {
    NOT_BANNED("NOT_BANNED"), BAN("BAN");

    private final String action;

    BanStatus(String action) {
        this.action = action;
    }

}

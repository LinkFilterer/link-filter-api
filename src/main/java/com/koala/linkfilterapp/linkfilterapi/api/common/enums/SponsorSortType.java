package com.koala.linkfilterapp.linkfilterapi.api.common.enums;

public enum SponsorSortType {
    url("url");

    private final String sortType;

    private SponsorSortType(String sortType) {
        this.sortType = sortType;
    }
}

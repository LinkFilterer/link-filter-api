package com.koala.linkfilterapp.linkfilterapi.api.common.enums;

public enum LinkSortType {
    url("url"), securityLevel("securityLevel");

    private final String sortType;

    private LinkSortType(String sortType) {
        this.sortType = sortType;
    }
}

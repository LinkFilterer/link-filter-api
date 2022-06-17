package com.koala.linkfilterapp.linkfilterapi.api.link.enums;

public enum LinkSortType {
    url("url"), securityLevel("securityLevel"), status("status"), badCount("badCount"),
    creationDate("creationDate"), modifiedDate("modifiedDate"), description("description"), isConnectable("isConnectable"),
    statusCode("statusCode"), lastMaintenance("lastMaintenance"), whoIsDate("whoIsDate");

    private final String sortType;

    LinkSortType(String sortType) {
        this.sortType = sortType;
    }
}

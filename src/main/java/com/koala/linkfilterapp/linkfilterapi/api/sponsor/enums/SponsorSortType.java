package com.koala.linkfilterapp.linkfilterapi.api.sponsor.enums;

public enum SponsorSortType {
    id("id"), description("description"), banner("banner"), url("url"),
    creationDate("creationDate"), endDate("endDate"), isExpired("isExpired"), weight("weight");

    private final String sortType;

    SponsorSortType(String sortType) {
        this.sortType = sortType;
    }
}

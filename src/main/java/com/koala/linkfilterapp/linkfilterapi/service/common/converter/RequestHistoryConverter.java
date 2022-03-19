package com.koala.linkfilterapp.linkfilterapi.service.common.converter;

import com.koala.linkfilterapp.linkfilterapi.api.requesthistory.entity.RequestHistory;
import com.koala.linkfilterapp.linkfilterapi.api.link.entity.Link;

import java.sql.Timestamp;

public class RequestHistoryConverter {
    public static RequestHistory convertLinkToRequestHistory(Link link) {
        RequestHistory history = new RequestHistory();
        history.setRequestTime(new Timestamp(System.currentTimeMillis()));
        return history;
    }
}

package com.koala.linkfilterapp.linkfilterapi.service.requesthistory.converter;

import com.koala.linkfilterapp.linkfilterapi.api.link.entity.Link;
import com.koala.linkfilterapp.linkfilterapi.api.requesthistory.entity.RequestHistory;

import java.sql.Timestamp;

public class RequestHistoryConverter {
    public static RequestHistory convertLinkToRequestHistory(Link link) {
        RequestHistory history = new RequestHistory();
        history.setRequestTime(new Timestamp(System.currentTimeMillis()));
        return history;
    }
}

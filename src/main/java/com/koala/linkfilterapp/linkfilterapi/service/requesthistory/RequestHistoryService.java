package com.koala.linkfilterapp.linkfilterapi.service.requesthistory;

import com.koala.linkfilterapp.linkfilterapi.api.link.entity.Link;

public interface RequestHistoryService {
    void saveRequestHistory(Link link, String ipAddress);
}

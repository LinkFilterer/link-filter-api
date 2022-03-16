package com.koala.linkfilterapp.linkfilterapi.service.sponsor;

import com.koala.linkfilterapp.linkfilterapi.api.common.exception.LinkException;
import com.koala.linkfilterapp.linkfilterapi.api.sponsor.dto.response.SponsorBean;

public interface SponsorService {
    SponsorBean getSponsorInfo(String projectName, String ipAddress) throws LinkException;
}

package com.koala.linkfilterapp.linkfilterapi.service.link;

import java.util.List;

public interface LinkValidationService {
    List<String> validateLinkRequest(String url, String ipAddress);
}

package com.koala.linkfilterapp.linkfilterapi.service.link.maintenance;

import com.koala.linkfilterapp.linkfilterapi.api.link.enums.LinkStatus;
import com.koala.linkfilterapp.linkfilterapi.api.link.dto.request.CreateLinkRequest;
import com.koala.linkfilterapp.linkfilterapi.repository.LinkRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LinkMaintenanceServiceTest {

    @InjectMocks
    LinkMaintenanceService service;

    @Mock
    LinkRepository repository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldConvertSaveRequest() {
        CreateLinkRequest request = new CreateLinkRequest();
        request.setUrl("test.com");
        request.setStatus(LinkStatus.VERIFIED);
        request.setSecurityLevel(10);
        service.createLink(request);

    }
}

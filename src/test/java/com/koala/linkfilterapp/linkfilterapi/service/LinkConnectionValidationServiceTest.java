package com.koala.linkfilterapp.linkfilterapi.service;

import com.koala.linkfilterapp.linkfilterapi.service.link.impl.LinkConnectionValidationService;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.logging.Logger;


public class LinkConnectionValidationServiceTest {
    Logger log = Logger.getLogger("LinkVerificationServiceTest");

    @InjectMocks
    LinkConnectionValidationService service;



    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }


}

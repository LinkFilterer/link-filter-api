package com.koala.linkfilterapp.linkfilterapi.service.link.impl;

import com.koala.linkfilterapp.linkfilterapi.api.link.entity.Link;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class LinkWhoIsServiceTest {
    Logger log = Logger.getLogger("LinkWhoIsServiceTest");

    @InjectMocks
    LinkWhoIsService service;

    @Before
    public void init() { MockitoAnnotations.initMocks(this); }

    @Test
    public void shouldSetWhois() {
        //  "wikipedia.org", , "amazon.co.uk",
        String[] inputs = new String[]{"google.com", "slickdeals.net", "discord.gg"};
        int iterations = 10;
        List<String> inputList = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {
            inputList.addAll(Arrays.asList(inputs));
        }
        String formattedResult = "Url: %s, WhoisDate: %s, SystemTime: %d \n";

        List<String> res = inputList.parallelStream().map(url -> {
            Link entity = new Link();
            entity.setUrl(url);
            service.setWhoIsDate(entity);
            return String.format(formattedResult, url, isNull(entity.getWhoIsDate()) ? "null" : entity.getWhoIsDate(), (int) System.currentTimeMillis());
        }).collect(Collectors.toList());

        log.info(res + " requests made: " + res.size());
    }

    @Test
    public void shouldSetWhois_Single() {
        String url = "bankofengland.co.uk";
        String formattedResult = "Url: %s, WhoisDate: %s, SystemTime: %d \n";

        Link entity = new Link();
        entity.setUrl(url);
        service.setWhoIsDate(entity);

        String res = String.format(formattedResult, url, isNull(entity.getWhoIsDate()) ? "null" : entity.getWhoIsDate(), (int) System.currentTimeMillis());
        log.info(res);
    }
}

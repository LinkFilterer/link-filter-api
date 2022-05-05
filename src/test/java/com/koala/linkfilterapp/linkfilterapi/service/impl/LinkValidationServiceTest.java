package com.koala.linkfilterapp.linkfilterapi.service.impl;

import com.koala.linkfilterapp.linkfilterapi.api.link.entity.Link;
import com.koala.linkfilterapp.linkfilterapi.service.link.impl.LinkValidationServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LinkValidationServiceTest {
    @InjectMocks
    LinkValidationServiceImpl service;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldValidateLink() {
        String url = "asdfasdf";
        String ip = "1234";

        List<String> res = service.validateLinkRequest(url, ip);
        assertFalse(res.isEmpty());

        url = "google.com";
        res = service.validateLinkRequest(url, ip);
        assertTrue(res.isEmpty());
    }

    @Test
    public void shouldTest() {
        Link link = new Link();
//        String test = "STATION STCD";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://google.com")
//                        .pathSegment("BLAH").pathSegment("TEST");
//        System.out.println(test.substring(0,6));
//        System.out.println(test.substring(1,2));
//        System.out.println(builder.build().encode().toString());
        long time = new Date().getTime();
        Date upper = new Date((time) + 72 * 60000L);
        Date lower = new Date((time) - 72 * 60000L);
        List<String> testList = new ArrayList<>();
        testList.add("b");
        testList.add("a");
        System.out.println(testList.stream().sorted().collect(Collectors.toList()));
        System.out.format(StringUtils.leftPad("", 3,'0'));
        System.out.println(upper.before(new Date()) + " " + lower.after(new Date())  );
        if (false || false) {

        }
//        List<Integer> testList = new ArrayList<>();
//        testList.add(1);
//        testList.add(2);
//        testList.add(5);
//
//        System.out.println(testList.stream().sorted().collect(Collectors.toList()));
//        System.out.println(lower);
//        System.out.println(upper);

//        System.out.println(test.split(" ")[0]);
////        String date = "19970516";
////        String formatted = String.format("%s-%s-%s", date.substring(0,4), date.substring(4,6), date.substring(6,8));
////        System.out.println(formatted);
////
//        List<Link> l1 = new ArrayList<>();
//        List<Link> l2 = new ArrayList<>();
//
////        LongAdder tieCode = new LongAdder();
////        for (int i = 0; i < 4; i++) {
////            System.out.println(tieCode);
////            tieCode.add(50);
////        }
////
//        l1.add(link);
//        l1.add(link);
//        l2.addAll(l1);
//
//        System.out.println(l1);
//        System.out.println(l2);
//
//        link.setUrl("Url");
//
//        System.out.println(l1);
//        System.out.println(l2);


    }
}

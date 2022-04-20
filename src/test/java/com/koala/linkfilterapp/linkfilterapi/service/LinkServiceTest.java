package com.koala.linkfilterapp.linkfilterapi.service;


import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

public class LinkServiceTest {
//    RequestHistory requestHistory;
//    Link link;
//
//    @InjectMocks
//    LinkServiceImpl service;
//
//    @Mock
//    LinkRepository repository;
//
//    @Mock
//    RequestHistoryServiceImpl requestHistoryService;
//
//    @Mock
//    ReportService reportService;
//
//    @Mock
//    LinkConnectionValidationService verificationService;
//
//    @Before
//    public void initEntities() {
//        requestHistory = new RequestHistory();
//        link = new Link();
//    }
//
//    @Before
//    public void init() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void shouldCheckLink() throws LinkException {
//        when(requestHistoryService.saveRequestHistory(anyString(), anyString(), any())).thenReturn(requestHistory);
//        link.setStatus(LinkStatus.VERIFIED);
//        when(repository.findById(anyString())).thenReturn(Optional.of(link));
//        LinkBean result = service.checkLink("TESTURL.com", "255.255.255.255");
//        assertEquals(result.getStatus(), LinkStatus.VERIFIED);
//    }


    @Test
    public void test() {
        String s="MANDAN      ND 600 120 120";
        String s1[]=s.split("[ ]+");
        List<String> splitArray = Arrays.asList(s1);
        System.out.println(splitArray);
        System.out.println(Integer.parseInt("1970981".substring(0, 7)));

//        if ((true) )
//        System.out.println(String.format("%s %s", "HI"));
//        System.out.println(convertDateToString(new Timestamp(System.currentTimeMillis())));
//        Integer val1 = 50;
//        Integer val2 = 100;
//       System.out.println(val1.compareTo(val2));
//
//       String url = "https://something.com/{empId}";
//       Map<String, String> params = new HashMap<>();
//       params.put("empId", "1234567");
//       URI processed = UriComponentsBuilder.fromHttpUrl(url).buildAndExpand(params).toUri();
//       System.out.println(processed);
    }

}

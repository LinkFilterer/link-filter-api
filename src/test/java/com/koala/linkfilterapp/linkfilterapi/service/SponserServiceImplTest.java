package com.koala.linkfilterapp.linkfilterapi.service;

import com.koala.linkfilterapp.linkfilterapi.api.sponsor.dto.response.SponsorBean;
import com.koala.linkfilterapp.linkfilterapi.api.sponsor.entity.Sponsor;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.LinkException;
import com.koala.linkfilterapp.linkfilterapi.repository.SponsorRepository;
import com.koala.linkfilterapp.linkfilterapi.service.sponsor.impl.SponsorServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

public class SponserServiceImplTest {
    Sponsor sponsor1;
    Sponsor sponsor2;
    Sponsor sponsor3;
    List<Sponsor> mockResult;


    @InjectMocks
    SponsorServiceImpl service;

    @Mock
    SponsorRepository repository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void initSponsors() {
        sponsor1 = new Sponsor();
        sponsor2 = new Sponsor();
        sponsor3 = new Sponsor();
        mockResult = new ArrayList<>();


    }

    @Test
    public void shouldGetRandomSponsor() throws LinkException {
        sponsor1.setId("1");
        sponsor2.setId("2");
        sponsor3.setId("3");
        sponsor1.setWeight(5.0);
        sponsor2.setWeight(5.0);
        sponsor3.setWeight(1.0);

        mockResult.add(sponsor1);
        mockResult.add(sponsor2);
        mockResult.add(sponsor3);

        Map<String, Integer> resultMap = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            when(repository.findAll()).thenReturn(mockResult);
            SponsorBean result = service.getSponsorInfo();

            resultMap.put(result.getProjectName(), resultMap.get(result.getProjectName()) == null ? 0 : resultMap.get(result.getProjectName()) + 1);
        }

        System.out.print(resultMap);
    }
}

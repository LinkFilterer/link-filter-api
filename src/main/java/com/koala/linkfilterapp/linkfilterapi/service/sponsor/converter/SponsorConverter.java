package com.koala.linkfilterapp.linkfilterapi.service.sponsor.converter;

import com.koala.linkfilterapp.linkfilterapi.api.sponsor.dto.request.SponsorRequestBean;
import com.koala.linkfilterapp.linkfilterapi.api.sponsor.dto.response.SponsorBean;
import com.koala.linkfilterapp.linkfilterapi.api.sponsor.entity.Sponsor;
import org.springframework.stereotype.Component;

import java.util.Date;

import static java.util.Objects.nonNull;

@Component
public class SponsorConverter {
    public Sponsor convert(SponsorRequestBean bean) {
        Sponsor entity = new Sponsor();
        entity.setId(bean.getId());
        entity.setUrl(bean.getUrl());
        entity.setWeight(bean.getWeight());
        entity.setDescription(bean.getDescription());
        entity.setIsExpired(bean.getIsExpired());
        entity.setEndDate(bean.getEndDate());
        entity.setBanner(bean.getBanner());
        entity.setCreationDate(nonNull(bean.getCreationDate()) ? bean.getCreationDate() : new Date());
        return entity;
    }

    public SponsorBean convert(Sponsor entity) {
        SponsorBean bean = new SponsorBean();
        bean.setProjectName(entity.getId());
        bean.setDescription(entity.getDescription());
        bean.setBanner(entity.getBanner());
        bean.setUrl(entity.getUrl());
        return bean;
    }
}

package com.koala.linkfilterapp.linkfilterapi.service.link;

import com.koala.linkfilterapp.linkfilterapi.api.common.exception.LinkException;
import com.koala.linkfilterapp.linkfilterapi.api.link.dto.response.LinkBean;
import com.koala.linkfilterapp.linkfilterapi.api.link.entity.Link;

public class LinkConverter {
    public static Link convert(LinkBean bean) throws LinkException {
        Link link = new Link();
        link.setUrl(bean.getUrl());
        link.setDescription(bean.getDescription());
        link.setStatus(bean.getStatus());
        link.setSecurityLevel(bean.getRating());
        return link;
    }

    public static LinkBean convert(Link entity) {
        LinkBean bean = new LinkBean();
        bean.setUrl(entity.getUrl());
        bean.setStatus(entity.getStatus());
        bean.setDescription(entity.getDescription());
        bean.setRating(entity.getSecurityLevel());
        return bean;
    }
}

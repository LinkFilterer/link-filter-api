package com.koala.linkfilterapp.linkfilterapi.api.link.dto.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CheckLinksRequest implements Serializable {
    List<String> urls;
}

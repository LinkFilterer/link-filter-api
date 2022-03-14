package com.koala.linkfilterapp.linkfilterapi.api.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestResponse<T> {
    String htmlStatusCode;
    String message;
    private T entity;
    private List<String> errors;
}

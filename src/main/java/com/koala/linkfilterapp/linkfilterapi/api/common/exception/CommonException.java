package com.koala.linkfilterapp.linkfilterapi.api.common.exception;

import com.koala.linkfilterapp.linkfilterapi.api.link.entity.Link;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class CommonException extends Exception {
    HttpStatus httpStatus;
    String description;
    Link link;
    String requestType;
    List<String> errors;

    public CommonException(HttpStatus status, String message) {
        this.description = message;
        this.httpStatus = status;
    }
}

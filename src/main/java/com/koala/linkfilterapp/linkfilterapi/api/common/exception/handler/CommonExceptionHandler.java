package com.koala.linkfilterapp.linkfilterapi.api.common.exception.handler;

import com.koala.linkfilterapp.linkfilterapi.api.common.dto.response.RestResponse;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.LinkException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler {
    @ExceptionHandler(LinkException.class)
    public ResponseEntity<RestResponse<Exception>> handleLinkException(LinkException exception) {
        RestResponse<Exception> response = new RestResponse<Exception>(exception.getHttpStatus().toString(), exception.getDescription(),null, exception.getErrors());
        return new ResponseEntity<RestResponse<Exception>>(response, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse<Exception>> handleGenericException(Exception exception) {
        RestResponse<Exception> response = new RestResponse<Exception>(HttpStatus.BAD_REQUEST.toString(), exception.getMessage(), null, null);
        return new ResponseEntity<RestResponse<Exception>>(response, HttpStatus.OK);
    }
}

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
public class LinkException extends Exception {
    HttpStatus httpStatus;
    String description;
    Link link;
    String requestType;
    List<String> errors;
}

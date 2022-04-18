package com.koala.linkfilterapp.linkfilterapp.security.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class LoginRequest implements Serializable {
    @NotEmpty
    String email;

    @NotEmpty
    String password;
}

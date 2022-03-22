package com.koala.linkfilterapp.linkfilterapp.security.dto;

import lombok.Value;

@Value
public class JwtAuthenticationResponse {
    String accessToken;
    UserInfo user;
}

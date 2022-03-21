package com.koala.linkfilterapp.linkfilterapi.api.security.dto;

import lombok.Value;

@Value
public class JwtAuthenticationResponse {
    String accessToken;
    UserInfo user;
}

package com.koala.linkfilterapp.linkfilterapp.security.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/common")
public class OAuthLoginController {
    Logger log = Logger.getLogger("OAuthLoginController");

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        log.info(principal.toString());
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }
}

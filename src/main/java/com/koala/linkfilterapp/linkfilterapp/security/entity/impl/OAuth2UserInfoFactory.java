package com.koala.linkfilterapp.linkfilterapp.security.entity.impl;

import com.koala.linkfilterapp.linkfilterapp.security.dto.SocialProvider;
import com.koala.linkfilterapp.linkfilterapp.security.entity.OAuth2UserInfo;
import com.koala.linkfilterapp.linkfilterapp.security.exception.OAuth2AuthenticationProcessingException;

import java.util.Map;


public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(SocialProvider.GITHUB.getProviderType())) {
            return new GithubOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
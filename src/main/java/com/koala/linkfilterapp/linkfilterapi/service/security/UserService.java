package com.koala.linkfilterapp.linkfilterapi.service.security;

import com.koala.linkfilterapp.linkfilterapi.api.security.dto.LocalUser;
import com.koala.linkfilterapp.linkfilterapi.api.security.dto.SignUpRequest;
import com.koala.linkfilterapp.linkfilterapi.api.security.entity.User;
import com.koala.linkfilterapp.linkfilterapi.api.security.exception.UserAlreadyExistAuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import java.util.Map;
import java.util.Optional;

public interface UserService {

    public User registerNewUser(SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException;

    User findUserByEmail(String email);

    Optional<User> findUserById(Long id);

    LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo);
}

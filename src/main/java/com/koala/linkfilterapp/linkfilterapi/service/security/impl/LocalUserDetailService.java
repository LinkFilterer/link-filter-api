package com.koala.linkfilterapp.linkfilterapi.service.security.impl;

import com.koala.linkfilterapp.linkfilterapi.api.security.dto.LocalUser;
import com.koala.linkfilterapp.linkfilterapi.api.security.entity.User;
import com.koala.linkfilterapp.linkfilterapi.api.security.exception.ResourceNotFoundException;
import com.koala.linkfilterapp.linkfilterapi.service.security.UserService;
import com.koala.linkfilterapp.linkfilterapi.service.security.utils.GeneralUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocalUserDetailService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public LocalUser loadUserByUsername(final String email) throws UsernameNotFoundException {
        User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " was not found in the database");
        }
        return createLocalUser(user);
    }

    @Transactional
    public LocalUser loadUserById(Long id) {
        User user = userService.findUserById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return createLocalUser(user);
    }

    /**
     * @param user
     * @return
     */
    private LocalUser createLocalUser(User user) {
        return new LocalUser(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, GeneralUtils.buildSimpleGrantedAuthorities(user.getRoles()), user);
    }

}
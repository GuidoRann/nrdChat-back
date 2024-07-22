package com.nrdChat.app.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IMyUserDetailsService extends UserDetailsService {

    @Override
    UserDetails loadUserByUsername(String username);
}

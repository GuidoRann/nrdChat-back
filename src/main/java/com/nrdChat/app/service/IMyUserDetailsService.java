package com.nrdChat.app.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface IMyUserDetailsService {

    UserDetails loadUserByUsername(String username);
}

package com.nrdChat.app.model;

import com.nrdChat.app.security.MyUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;

public class UserPrincipal implements Principal {

    private final MyUserDetails userDetails;

    public UserPrincipal(MyUserDetails userDetails) {
        this.userDetails = userDetails;
    }

    @Override
    public String getName() {
        return userDetails.getUsername();
    }

    public String getDisplayName() {
        return userDetails.getDisplayName();
    }

    public MyUserDetails getUserDetails() {
        return userDetails;
    }

    public UserChat getUserChat() {
        return userDetails.getUser();
    }

    public static UserPrincipal current() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ( authentication != null && authentication.getPrincipal() instanceof MyUserDetails user ) {
            return new UserPrincipal( user );
        }
        return null;
    }
}

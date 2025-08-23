package com.nrdChat.app.webSocket;

import com.nrdChat.app.model.UserPrincipal;
import com.nrdChat.app.security.MyUserDetails;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Component
public class PrincipalHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {

        MyUserDetails myUserDetails = (MyUserDetails) attributes.get("authUser");
        return new UserPrincipal( myUserDetails );
    }
}

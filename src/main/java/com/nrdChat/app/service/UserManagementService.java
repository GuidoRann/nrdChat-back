package com.nrdChat.app.service;

import com.nrdChat.app.dtos.ReqRes;
import com.nrdChat.app.enums.UserState;
import com.nrdChat.app.model.UserChat;
import com.nrdChat.app.repository.UserRepository;
import com.nrdChat.app.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserManagementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ReqRes registerUser(ReqRes registrationRequest) {
        ReqRes resp = new ReqRes();

        try {
            UserChat userChat = UserChat.builder()
                    .username(registrationRequest .getUsername())
                    .email(registrationRequest .getEmail())
                    .password(passwordEncoder.encode(registrationRequest.getPassword()))
                    .userState(UserState.OFFLINE)
                    .build();

            UserChat userRepo = userRepository.save(userChat);

            if (userRepo.getUserId() > 0) {
                resp.setUserChat(userRepo);
                resp.setStatusCode(200);
                resp.setMessage("User saved successfully");
            }

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }


        return resp;
    }


}

package com.nrdChat.app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nrdChat.app.enums.UserRole;
import com.nrdChat.app.enums.UserState;
import com.nrdChat.app.model.UserChat;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqRes {

    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String username;
    private String email;
    private UserState userState;
    private UserRole role;
    private String password;
    private UserChat userChat;
    private List<UserChat> userChatList;

}

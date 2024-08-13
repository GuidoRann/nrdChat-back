package com.nrdChat.app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nrdChat.app.enums.FriendState;
import com.nrdChat.app.model.FriendEntity;
import com.nrdChat.app.model.UserChat;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FriendsDTO {

    private int statusCode;
    private String error;
    private String message;
    private UserChat user;
    private FriendEntity friend;
    private FriendState friendState;
}

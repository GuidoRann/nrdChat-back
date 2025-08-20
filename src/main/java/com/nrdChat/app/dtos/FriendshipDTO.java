package com.nrdChat.app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nrdChat.app.enums.FriendshipState;
import com.nrdChat.app.model.FriendshipEntity;
import com.nrdChat.app.model.UserChat;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FriendshipDTO {

    private int statusCode;
    private String error;
    private String message;
    private UserChat user;
    private UserChat friend;
    private FriendshipEntity friendship;
    private List<FriendshipEntity> AcceptedFriendsList;
    private List<FriendshipEntity> PendingFriendsList;
    private List<FriendshipEntity> SentRequestsList;
    private FriendshipState friendshipState;
}

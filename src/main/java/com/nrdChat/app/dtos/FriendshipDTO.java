package com.nrdChat.app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nrdChat.app.enums.FriendshipState;
import com.nrdChat.app.model.FriendshipEntity;
import com.nrdChat.app.model.UserChat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FriendshipDTO {

    private int statusCode;
    private String error;
    private String message;

    private UserDTO user;
    private UserDTO friend;

    private FriendshipDTO friendship;
    private List<FriendshipDTO> AcceptedFriendsList;
    private List<FriendshipDTO> PendingFriendsList;
    private List<FriendshipDTO> SentRequestsList;

    private FriendshipState friendshipState;
}

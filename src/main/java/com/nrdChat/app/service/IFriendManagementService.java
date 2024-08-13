package com.nrdChat.app.service;

import com.nrdChat.app.dtos.FriendsDTO;
import com.nrdChat.app.model.UserChat;

public interface IFriendManagementService {

    FriendsDTO saveFriend(UserChat friends, UserChat userChat);

    public FriendsDTO deleteFriend(Long friendsId);
}

package com.nrdChat.app.service;

import com.nrdChat.app.dtos.FriendshipDTO;
import com.nrdChat.app.model.UserChat;


public interface IFriendshipManagementService {

    FriendshipDTO saveFriend( UserChat user, UserChat friend );

    FriendshipDTO getAllAcceptedFriends( String email );

    FriendshipDTO getAllPendingFriends( String email );

    FriendshipDTO getAllSentRequests( String email );

    FriendshipDTO acceptFriend( UserChat user, UserChat friend );

    void deleteFriend( UserChat user, UserChat friend );

}

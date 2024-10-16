package com.nrdChat.app.service;

import com.nrdChat.app.dtos.FriendshipDTO;
import com.nrdChat.app.model.UserChat;


public interface IFriendshipManagementService {

    FriendshipDTO saveFriend( UserChat user, UserChat friend );

    FriendshipDTO getFriendList( String email );

    FriendshipDTO getFriendRequests( String email );

    FriendshipDTO acceptFriend( UserChat user, UserChat friend );

    FriendshipDTO deleteFriend( UserChat user, UserChat friend );
}

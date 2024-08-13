package com.nrdChat.app.service;

import com.nrdChat.app.dtos.FriendsDTO;
import com.nrdChat.app.enums.FriendState;
import com.nrdChat.app.model.FriendEntity;
import com.nrdChat.app.model.UserChat;
import com.nrdChat.app.repository.FriendRepository;
import com.nrdChat.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FriendManagementService implements IFriendManagementService {

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public FriendsDTO saveFriend(UserChat friends, UserChat userChat) {
        FriendsDTO resp = new FriendsDTO();

        try {
            FriendEntity newFriend = new FriendEntity();
            newFriend.setFriend(friends);
            newFriend.setUser(userChat);
            newFriend.setFriendState(FriendState.PENDING);
            friendRepository.save(newFriend);
            resp.setStatusCode(200);
            resp.setMessage("Friend added successfully");
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setMessage("Error occurred: " + e.getMessage());
        }

        return resp;
    }

    @Override
    public FriendsDTO deleteFriend(Long friendId) {
        FriendsDTO resp = new FriendsDTO();

        try {
            Optional<FriendEntity> friend = friendRepository.findById(friendId);

            if (friend.isPresent()){
                friendRepository.delete(friend.get());
                resp.setStatusCode(200);
                resp.setMessage("Friend deleted successfully");
            } else {
                resp.setStatusCode(404);
                resp.setMessage("Friend not found");
            }

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setMessage("Error occurred: " + e.getMessage());
        }

        return resp;
    }
}

package com.nrdChat.app.service;

import com.nrdChat.app.dtos.UserDTO;
import com.nrdChat.app.enums.UserState;
import com.nrdChat.app.model.UserChat;
import com.nrdChat.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserChatService implements IUserChatService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDTO createUser(UserDTO userDTO) {

        UserChat userChat = UserChat.builder()
                .userName(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .friendEntityList(null)
                .sentMessageEntities(null)
                .receivedMessageEntities(null)
                .userState(UserState.OFFLINE)
                .build();

        userRepository.save(userChat);
        return userDTO;
    }
}

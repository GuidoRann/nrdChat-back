package com.nrdChat.app.service;

import com.nrdChat.app.dtos.UserDTO;
import com.nrdChat.app.dtos.UserRegistrationDTO;
import com.nrdChat.app.model.UserChat;

public interface IUserManagementService {
    public UserDTO registerUser(UserRegistrationDTO registrationRequest);

    public UserDTO loginUser(UserRegistrationDTO loginRequest);

    public UserDTO refreshToken(UserDTO refreshTokenRequest);

    UserDTO getUserByEmail(String email);

    public UserDTO deleteUserById(Long userId);

    public UserDTO updateUser(Long userId, UserChat updatedUserChat);

    public UserDTO getMyInfo(String email);
}

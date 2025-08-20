package com.nrdChat.app.service;

import com.nrdChat.app.dtos.UserDTO;
import com.nrdChat.app.model.UserChat;

public interface IUserManagmentService {
    public UserDTO registerUser(UserDTO registrationRequest);

    public UserDTO loginUser(UserDTO loginRequest);

    public UserDTO refreshToken(UserDTO refreshTokenRequest);

    public UserDTO getAllUsers();

    UserDTO getUserByEmail(String email);

    public UserDTO deleteUserById(Long userId);

    public UserDTO updateUser(Long userId, UserChat updatedUserChat);

    public UserDTO getMyInfo(String email);
}

package com.nrdChat.app.service;

import com.nrdChat.app.dtos.UserDTO;
import com.nrdChat.app.enums.UserRole;
import com.nrdChat.app.enums.UserState;
import com.nrdChat.app.model.UserChat;
import com.nrdChat.app.repository.UserRepository;
import com.nrdChat.app.security.jwt.JwtUtils;
import com.nrdChat.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserManagementService implements IUserManagmentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityConfig securityConfig;

    @Override
    public UserDTO registerUser(UserDTO registrationRequest) {
        UserDTO resp = new UserDTO();

        try {
            UserChat userChat = UserChat.builder()
                    .name(registrationRequest.getName())
                    .email(registrationRequest.getEmail())
                    .password(securityConfig.passwordEncoder().encode(registrationRequest.getPassword()))
                    .role(UserRole.USER.toString())
                    .userState(UserState.OFFLINE)
                    .build();

            UserChat userRepo = userRepository.save(userChat);

            if (userRepo.getUserId() > 0) {
                resp.setUserChat(userRepo);
                resp.setStatusCode(200);
                resp.setMessage("User saved successfully");
            }

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }

        return resp;
    }

    @Override
    public UserDTO loginUser(UserDTO loginRequest) {
        UserDTO resp = new UserDTO();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword()
                    )
            );

            Optional<UserChat> userOpt = userRepository.findByEmail(loginRequest.getEmail());

            if (userOpt.isPresent()) {
                var user = userOpt.get();
                var token = jwtUtils.generateToken(user);
                var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

                resp.setStatusCode(200);
                resp.setToken(token);
                resp.setRole(user.getRole());
                resp.setRefreshToken(refreshToken);
                resp.setExpirationTime("24h");
                resp.setMessage("User logged in successfully");
                resp.setUserState(UserState.ONLINE);
            } else {
                resp.setStatusCode(401);
                resp.setMessage("Invalid email or password");
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setMessage(e.getMessage());
        }
        return resp;
    }

    @Override
    public UserDTO refreshToken(UserDTO refreshTokenRequest) {
        UserDTO resp = new UserDTO();
        try {
            String userEmail = jwtUtils.extractUsername(refreshTokenRequest.getRefreshToken());
            var user = userRepository.findByEmail(userEmail).orElseThrow();

            if (jwtUtils.isTokenValid(refreshTokenRequest.getRefreshToken(), user)) {
                var token = jwtUtils.generateToken(user);
                resp.setStatusCode(200);
                resp.setToken(token);
                resp.setRefreshToken(refreshTokenRequest.getRefreshToken());
                resp.setExpirationTime("24h");
                resp.setMessage("User logged in successfully");
                user.setUserState(UserState.ONLINE);
            }

            resp.setStatusCode(200);
            return resp;

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setMessage(e.getMessage());
            return resp;
        }
    }

    @Override
    public UserDTO getAllUsers() {
        UserDTO resp = new UserDTO();
        try {
            List<UserChat> userChatList = userRepository.findAll();
            if(!userChatList.isEmpty()){
                resp.setUserChatList(userChatList);
                resp.setStatusCode(200);
                resp.setMessage("Successfully found users");
            } else {
                resp.setStatusCode(404);
                resp.setMessage("No users found");
            }
            return resp;

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setMessage("Error occurred: " + e.getMessage());
            return resp;
        }
    }

    @Override
    public UserDTO getUserByEmail(String email){
        UserDTO resp = new UserDTO();

        try {
            UserChat user = userRepository.findByEmail(email).orElseThrow( () -> new RuntimeException("User not found")); ;
            resp.setUserChat(user);
            resp.setStatusCode(200);
            resp.setMessage("User found successfully");
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setMessage("Error occurred: " + e.getMessage());
        }
        return resp;
    }

    @Override
    public UserDTO deleteUserById (Long userId){
        UserDTO resp = new UserDTO();
        try {
            Optional<UserChat> user = userRepository.findById(userId);
            if(user.isPresent()){
                userRepository.delete(user.get()); // try with deleteById if not working
                resp.setStatusCode(200);
                resp.setMessage("User deleted successfully");
            }else {
                resp.setStatusCode(404);
                resp.setMessage("User not found");
            }

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setMessage("Error occurred: " + e.getMessage());
        }
        return resp;
    }

    @Override
    public UserDTO updateUser(Long userId, UserChat updatedUserChat){
        UserDTO resp = new UserDTO();
        try {
            Optional<UserChat> user = userRepository.findById(userId);

            if(user.isPresent()){
                UserChat existingUserChat = user.get();
                existingUserChat.setName(updatedUserChat.getName());
                existingUserChat.setEmail(updatedUserChat.getEmail());
                existingUserChat.setRole(updatedUserChat.getRole());

                //Check if password is not null and not empty
                if(updatedUserChat.getPassword() != null && !updatedUserChat.getPassword().isEmpty()){
                    //Encode password before saving and update
                    existingUserChat.setPassword(securityConfig.passwordEncoder().encode(updatedUserChat.getPassword()));
                }

                UserChat savedUserChat = userRepository.save(existingUserChat);
                resp.setUserChat(savedUserChat);
                resp.setStatusCode(200);
                resp.setMessage("User updated successfully");
            }else {
                resp.setStatusCode(404);
                resp.setMessage("User not found");
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setMessage("Error occurred: " + e.getMessage());
        }
        return resp;
    }

    @Override
    public UserDTO getMyInfo(String email){
        UserDTO resp = new UserDTO();

        try {
            Optional<UserChat> user = userRepository.findByEmail(email);
            if(user.isPresent()){
                resp.setUserChat(user.get());
                resp.setStatusCode(200);
                resp.setMessage("User found successfully");
            }else {
                resp.setStatusCode(404);
                resp.setMessage("User not found");
            }

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setMessage("Error occurred: " + e.getMessage());
        }
        return resp;
    }
}

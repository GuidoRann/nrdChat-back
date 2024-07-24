package com.nrdChat.app.service;

import com.nrdChat.app.dtos.ReqRes;
import com.nrdChat.app.enums.UserState;
import com.nrdChat.app.model.UserChat;
import com.nrdChat.app.repository.UserRepository;
import com.nrdChat.app.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserManagementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ReqRes registerUser(ReqRes registrationRequest) {
        ReqRes resp = new ReqRes();

        try {
            UserChat userChat = UserChat.builder()
                    .username(registrationRequest.getUsername())
                    .email(registrationRequest.getEmail())
                    .password(passwordEncoder.encode(registrationRequest.getPassword()))
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

    public ReqRes loginUser(ReqRes loginRequest) {
        ReqRes resp = new ReqRes();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword()
                    )
            );
            var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
            var token = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

            resp.setStatusCode(200);
            resp.setToken(token);
            resp.setRefreshToken(refreshToken);
            resp.setExpirationTime("24h");
            resp.setMessage("User logged in successfully");
            user.setUserState(UserState.ONLINE);

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setMessage(e.getMessage());
        }
        return resp;
    }

    public ReqRes refreshToken(ReqRes refreshTokenRequest) {
        ReqRes resp = new ReqRes();
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

    public ReqRes getAllUsers(){
        ReqRes resp = new ReqRes();
        try {
            List<UserChat> userChatList = userRepository.findAll();
            if(!userChatList.isEmpty()){
                resp.setUserChatList(userChatList);
                resp.setStatusCode(200);
                resp.setMessage("Successfully found users");
                return resp;
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

    public ReqRes getUserById (Long userId){
        ReqRes resp = new ReqRes();
        try {
            UserChat user = userRepository.findById(userId).orElseThrow( () -> new RuntimeException("User not found")); ;
            resp.setUserChat(user);
            resp.setStatusCode(200);
            resp.setMessage("User found successfully");
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setMessage("Error occurred: " + e.getMessage());
        }

        return resp;
    }

    public ReqRes deleteUserById (Long userId){
        ReqRes resp = new ReqRes();
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

    public ReqRes updateUser(Long userId, UserChat updatedUserChat){
        ReqRes resp = new ReqRes();
        try {
            Optional<UserChat> user = userRepository.findById(userId);
            if(user.isPresent()){

                UserChat existingUserChat = user.get();
                existingUserChat.setUsername(updatedUserChat.getUsername());
                existingUserChat.setEmail(updatedUserChat.getEmail());
                existingUserChat.setPassword(updatedUserChat.getPassword());
                existingUserChat.setRole(updatedUserChat.getRole());
                existingUserChat.setUserState(updatedUserChat.getUserState());

                //Check if password is not null and not empty
                if(updatedUserChat.getPassword() != null && !updatedUserChat.getPassword().isEmpty()){
                    //Encode password before saving and update
                    existingUserChat.setPassword(passwordEncoder.encode(updatedUserChat.getPassword()));
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

    public ReqRes getMyInfo(String email){
        ReqRes resp = new ReqRes();
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

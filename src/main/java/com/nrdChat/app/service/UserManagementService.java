package com.nrdChat.app.service;

import com.nrdChat.app.dtos.MessageDTO;
import com.nrdChat.app.dtos.UserDTO;
import com.nrdChat.app.dtos.UserRegistrationDTO;
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
public class UserManagementService implements IUserManagementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityConfig securityConfig;

    @Override
    public UserDTO registerUser(UserRegistrationDTO registrationRequest) {
        UserDTO resp;

        try {
            UserChat userChat = UserChat.builder()
                    .name( registrationRequest.getName() )
                    .email( registrationRequest.getEmail() )
                    .password( securityConfig.passwordEncoder().encode(registrationRequest.getPassword()) )
                    .role( UserRole.USER.toString() )
                    .userState( UserState.OFFLINE )
                    .build();

            UserChat userRepo = userRepository.save(userChat);

            resp = UserDTO.builder()
                    .statusCode( userRepo.getUserId() > 0 ? 200 : 500 )
                    .message( userRepo.getUserId() > 0
                            ? "Message saved successfully"
                            : "Logic error: Message not saved" )
                    .build();

        } catch (Exception e) {
            resp = UserDTO.builder()
                    .statusCode( 500 )
                    .error( e.getMessage() )
                    .build();
        }

        return resp;
    }

    @Override
    public UserDTO loginUser(UserRegistrationDTO loginRequest) {
        UserDTO resp;

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword()
                    )
            );

            UserChat user = userRepository.findByEmail( loginRequest.getEmail() )
                    .orElseThrow( () -> new RuntimeException("Invalid email or password") );

            String token = jwtUtils.generateToken( user );
            String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user );

                resp = UserDTO.builder()
                        .statusCode( user.getUserId() > 0 ? 200 : 401 )
                        .message( user.getUserId() > 0
                                ? "User logged in successfully"
                                : "Logic error: Invalid email or password" )

                        .name( user.getName() )
                        .email( user.getEmail() )
                        .token( token )
                        .refreshToken( refreshToken )
                        .expirationTime( "24h" )
                        .role( user.getRole() )
                        .userState( UserState.ONLINE )
                        .build();

        } catch (Exception e) {
            resp = UserDTO.builder()
                    .statusCode( 500 )
                    .error( e.getMessage() )
                    .build();
        }

        return resp;
    }

    @Override
    public UserDTO refreshToken(UserDTO refreshTokenRequest) {
        UserDTO resp;
        try {
            String userEmail = jwtUtils.extractUsername( refreshTokenRequest.getRefreshToken() );
            var user = userRepository.findByEmail( userEmail ).orElseThrow();

            if (jwtUtils.isTokenValid(refreshTokenRequest.getRefreshToken(), user)) {
                var token = jwtUtils.generateToken(user);
                resp = UserDTO.builder()
                        .statusCode( 200 )
                        .token( token )
                        .refreshToken( refreshTokenRequest.getRefreshToken() )
                        .expirationTime("24h")
                        .message( "User logged in successfully" )
                        .name( user.getName() )
                        .email( user.getEmail() )
                        .role( user.getRole() )
                        .userState(UserState.ONLINE)
                        .build();
            } else {
                resp = UserDTO.builder()
                        .statusCode(401)
                        .message("Invalid or expired refresh token")
                        .build();
            }

        } catch (Exception e) {
            resp = UserDTO.builder()
                    .statusCode( 500 )
                    .error( e.getMessage() )
                    .build();

            return resp;
        }

        return resp;
    }

    @Override
    public UserDTO getUserByEmail( String email ){
        UserDTO resp;

        try {
            UserChat user = userRepository.findByEmail( email ).orElseThrow( () -> new RuntimeException( "User not found" ) );
            resp = UserDTO.builder()
                    .email( user.getEmail() )
                    .name( user.getName() )
                    .statusCode( user.getUserId() > 0 ? 200 : 404 )
                    .message( user.getUserId() > 0
                            ? "User found successfully"
                            : "Logic error: User not found" )
                    .build();

        } catch (Exception e) {
            resp = UserDTO.builder()
                    .statusCode( 500 )
                    .error( e.getMessage() )
                    .build();
        }

        return resp;
    }

    @Override
    public UserDTO deleteUserById ( Long userId ){
        UserDTO resp;

        try {
            UserChat user = userRepository.findById( userId ).orElseThrow( () -> new RuntimeException( "User not found") );

            userRepository.delete( user );

            resp = UserDTO.builder()
                    .statusCode( user.getUserId() > 0 ? 200 : 404 )
                    .message( user.getUserId() > 0
                            ? "User deleted successfully"
                            : "Logic error: User not found" )
                    .build();

        } catch (Exception e) {
            resp = UserDTO.builder()
                    .statusCode( 500 )
                    .error( e.getMessage() )
                    .build();
        }

        return resp;
    }

    @Override
    public UserDTO updateUser( Long userId, UserChat updatedUserChat ){
        UserDTO resp;
        try {
            UserChat existingUserChat = userRepository.findById( userId ).orElseThrow( () -> new RuntimeException( "User not found") );

                existingUserChat.setName( updatedUserChat.getName() );
                existingUserChat.setEmail( updatedUserChat.getEmail() );
                existingUserChat.setRole( updatedUserChat.getRole() );

                if( updatedUserChat.getPassword() != null && !updatedUserChat.getPassword().isEmpty() ){

                    //Encode password before saving and update
                    existingUserChat.setPassword( securityConfig.passwordEncoder().encode( updatedUserChat.getPassword() ) );
                }

                UserChat savedUserChat = userRepository.save( existingUserChat );
                resp = UserDTO.builder()
                        .statusCode( savedUserChat.getUserId() > 0 ? 200 : 404 )
                        .message( savedUserChat.getUserId() > 0
                                ? "User updated successfully"
                                : "Logic error: User not found" )
                        .build();

        } catch (Exception e) {
            resp = UserDTO.builder()
                    .statusCode( 500 )
                    .error( e.getMessage() )
                    .build();
        }

        return resp;
    }

    @Override
    public UserDTO getMyInfo(String email){
        UserDTO resp;

        try {
            UserChat user = userRepository.findByEmail( email ).orElseThrow( () -> new RuntimeException( "User not found") );
                resp = UserDTO.builder()
                        .email( user.getEmail() )
                        .name( user.getName() )
                        .role( user.getRole() )
                        .userState( user.getUserState() )
                        .statusCode( user.getUserId() > 0 ? 200 : 404 )
                        .message( user.getUserId() > 0
                                ? "User found successfully"
                                : "Logic error: User not found" )
                        .build();

        } catch (Exception e) {
            resp = UserDTO.builder()
                    .statusCode( 500 )
                    .error( e.getMessage() )
                    .build();
        }

        return resp;
    }

//   -----------OLD CODE-----------
//    @Override
//    public UserDTO getMyInfo(String email){
//        UserDTO resp = new UserDTO();
//
//        try {
//            Optional<UserChat> user = userRepository.findByEmail(email);
//            if(user.isPresent()){
//                resp.setUserChat(user.get());
//                resp.setStatusCode(200);
//                resp.setMessage("User found successfully");
//            }else {
//                resp.setStatusCode(404);
//                resp.setMessage("User not found");
//            }
//
//        } catch (Exception e) {
//            resp.setStatusCode(500);
//            resp.setMessage("Error occurred: " + e.getMessage());
//        }
//        return resp;
//    }

}

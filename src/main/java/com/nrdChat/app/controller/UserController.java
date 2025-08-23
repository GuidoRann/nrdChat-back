package com.nrdChat.app.controller;

import com.nrdChat.app.dtos.UserDTO;
import com.nrdChat.app.dtos.UserRegistrationDTO;
import com.nrdChat.app.model.UserChat;
import com.nrdChat.app.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserManagementService userManagementService;

    @PostMapping("/auth/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserRegistrationDTO registrationRequest) {
        return ResponseEntity.ok(userManagementService.registerUser(registrationRequest));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<UserDTO> loginUser(@RequestBody UserRegistrationDTO loginRequest) {
        return ResponseEntity.ok(userManagementService.loginUser(loginRequest));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<UserDTO> refreshToken(@RequestBody UserDTO userDTO){
      return ResponseEntity.ok(userManagementService.refreshToken(userDTO));
    }

    @GetMapping("/adminuser/get-user/{userEmail}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String userEmail) {
        return ResponseEntity.ok(userManagementService.getUserByEmail( userEmail ));
    }

    @GetMapping("/adminuser/update-user/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @RequestBody UserChat updatedUserChat) {
        return ResponseEntity.ok(userManagementService.updateUser(userId, updatedUserChat));
    }

    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<UserDTO> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserDTO resp = userManagementService.getMyInfo(email);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userManagementService.deleteUserById(userId));
    }

}

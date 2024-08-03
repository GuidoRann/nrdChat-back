package com.nrdChat.app.controller;

import com.nrdChat.app.dtos.UserDTO;
import com.nrdChat.app.model.UserChat;
import com.nrdChat.app.service.IUserManagmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    IUserManagmentService userManagementService;

    @PostMapping("/auth/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO registrationRequest) {
        UserDTO resp = userManagementService.registerUser(registrationRequest);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<UserDTO> loginUser(@RequestBody UserDTO loginRequest) {
        UserDTO resp = userManagementService.loginUser(loginRequest);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/user/get-user/{userId}")
    public ResponseEntity<UserDTO> getUsers(@PathVariable Long userId) {
        UserDTO resp = userManagementService.getUserById(userId);
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/adminuser/update-user/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @RequestBody UserChat updatedUserChat) {
        UserDTO resp = userManagementService.updateUser(userId, updatedUserChat);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/user/get-profile")
    public ResponseEntity<UserDTO> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserDTO resp = userManagementService.getMyInfo(email);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable Long userId) {
        UserDTO resp = userManagementService.deleteUserById(userId);
        return ResponseEntity.ok(resp);
    }

}

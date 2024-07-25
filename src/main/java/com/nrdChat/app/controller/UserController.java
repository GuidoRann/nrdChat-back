package com.nrdChat.app.controller;

import com.nrdChat.app.dtos.ReqRes;
import com.nrdChat.app.model.UserChat;
import com.nrdChat.app.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(value = "http://localhost:5173")
@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    UserManagementService userManagementService;

    @PostMapping("/auth/register")
    public ResponseEntity<ReqRes> registerUser(@RequestBody ReqRes registrationRequest) {
        ReqRes resp = userManagementService.registerUser(registrationRequest);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ReqRes> loginUser(@RequestBody ReqRes loginRequest) {
        ReqRes resp = userManagementService.loginUser(loginRequest);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/user/get-user/{userId}")
    public ResponseEntity<ReqRes> getUsers(@PathVariable Long userId) {
        ReqRes resp = userManagementService.getUserById(userId);
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/adminuser/update-user/{userId}")
    public ResponseEntity<ReqRes> updateUser(@PathVariable Long userId, @RequestBody UserChat updatedUserChat) {
        ReqRes resp = userManagementService.updateUser(userId, updatedUserChat);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/user/get-profile")
    public ResponseEntity<ReqRes> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ReqRes resp = userManagementService.getMyInfo(email);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<ReqRes> deleteUser(@PathVariable Long userId) {
        ReqRes resp = userManagementService.deleteUserById(userId);
        return ResponseEntity.ok(resp);
    }

}

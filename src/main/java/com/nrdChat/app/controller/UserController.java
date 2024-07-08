package com.nrdChat.app.controller;

import com.nrdChat.app.dtos.UserDTO;
import com.nrdChat.app.service.UserChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(value = "http://localhost:5173")
@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    UserChatService userChatService;

    @PostMapping("/createUser")
    public UserDTO createUser(@RequestBody UserDTO user) {
        return userChatService.createUser(user);
    }



}

package com.nrdChat.app.controller;

import com.nrdChat.app.dtos.MessageDTO;
import com.nrdChat.app.model.UserChat;
import com.nrdChat.app.service.MessageManagementService;
import com.nrdChat.app.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MessageController {

    @Autowired
    MessageManagementService messageManagementService;

    @Autowired
    UserManagementService userManagementService;

    @PostMapping( "/adminuser/save-message" )
    public ResponseEntity<MessageDTO> saveMessage( @RequestBody MessageDTO messageDTO ) {
        return ResponseEntity.ok( messageManagementService.saveMessage( messageDTO ) );
    }

    @DeleteMapping( "/adminuser/delete-message/{messageId}" )
    public ResponseEntity<MessageDTO> deleteMessage( @PathVariable Long messageId ) {
        return ResponseEntity.ok( messageManagementService.deleteMessage( messageId ) );
    }

    @GetMapping("/adminuser/get-messages")
    public ResponseEntity<MessageDTO> getAllMessages(
            @RequestParam String senderEmail,
            @RequestParam String receiverEmail) {

        UserChat sender = userManagementService.getUserByEmail( senderEmail ).getUserChat();
        UserChat receiver = userManagementService.getUserByEmail( receiverEmail ).getUserChat();

        return ResponseEntity.ok( messageManagementService.getAllMessages( sender, receiver ) );
    }
}

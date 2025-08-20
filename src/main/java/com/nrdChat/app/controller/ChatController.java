package com.nrdChat.app.controller;

import com.nrdChat.app.dtos.MessageDTO;
import com.nrdChat.app.service.MessageManagementService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageManagementService messageService;

    public ChatController(SimpMessagingTemplate simpMessagingTemplate, MessageManagementService messageService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.messageService = messageService;
    }

    @MessageMapping("/chat.sendPrivateMessage")
    public void sendPrivateMessage(@Payload MessageDTO message) {
        MessageDTO savedMessage = messageService.saveMessage( message );

        simpMessagingTemplate.convertAndSendToUser(
                savedMessage.getReceiver().getEmail(), "/queue/messages", savedMessage);
    }
}

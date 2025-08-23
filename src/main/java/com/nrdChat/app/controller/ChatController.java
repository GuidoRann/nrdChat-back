package com.nrdChat.app.controller;

import com.nrdChat.app.dtos.ChatMessageDTO;
import com.nrdChat.app.dtos.MessageDTO;
import com.nrdChat.app.mapper.DtoMapper;
import com.nrdChat.app.model.UserPrincipal;
import com.nrdChat.app.service.MessageManagementService;
import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageManagementService messageService;

    public ChatController(SimpMessagingTemplate simpMessagingTemplate, MessageManagementService messageService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.messageService = messageService;
    }

    @MessageMapping("/chat.sendPrivateMessage")
    public void sendPrivateMessage(@Payload MessageDTO message, Principal principal) {

        var up = (UserPrincipal) principal;
        var senderUser = up.getUserChat();
        var displayName = up.getDisplayName();

        message.setSender(DtoMapper.toUserDTO(senderUser));

        MessageDTO savedMessage = messageService.saveMessage(message);

        simpMessagingTemplate.convertAndSendToUser(
                savedMessage.getReceiver().getEmail(),
                "/queue/messages",
                savedMessage
        );
    }

    @GetMapping("/api/messages")
    public Page<ChatMessageDTO> getHistory(
            @RequestParam Long userA,
            @RequestParam Long userB,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        // page por sendDate desc; mapeás a DTO
        return null;
    }
}

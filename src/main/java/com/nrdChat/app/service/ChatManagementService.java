package com.nrdChat.app.service;
import com.nrdChat.app.dtos.ChatMessageDTO;
import com.nrdChat.app.enums.MessageState;
import com.nrdChat.app.model.MessageEntity;
import com.nrdChat.app.model.UserChat;
import com.nrdChat.app.repository.MessageRepository;
import com.nrdChat.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatManagementService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public ChatMessageDTO saveMessage(ChatMessageDTO dto) {

        UserChat sender = userRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        UserChat receiver = userRepository.findById(dto.getReceiverId())
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        MessageEntity entity = MessageEntity.builder()
                .content(dto.getContent())
                .urlImg(dto.getUrlImg())
                .messageType(dto.getMessageType())
                .messageState(dto.getMessageState() != null ? dto.getMessageState() : MessageState.SENT)
                .sendDate(LocalDateTime.now())
                .sender(sender)
                .receiver(receiver)
                .build();

        entity = messageRepository.save(entity);

        return ChatMessageDTO.builder()
                .messageId(entity.getMessageId())
                .senderId(sender.getUserId())
                .receiverId(receiver.getUserId())
                .content(entity.getContent())
                .urlImg(entity.getUrlImg())
                .messageType(entity.getMessageType())
                .messageState(entity.getMessageState())
                .sendDate(entity.getSendDate())
                .build();
    }
}

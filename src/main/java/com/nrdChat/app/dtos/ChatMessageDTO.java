package com.nrdChat.app.dtos;

import com.nrdChat.app.enums.MessageState;
import com.nrdChat.app.enums.MessageType;
import lombok.*;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ChatMessageDTO {
    private Long messageId;
    private Long senderId;
    private Long chatId;
    private Long receiverId;
    private String content;
    private String urlImg;
    private MessageType messageType;
    private MessageState messageState;
    private LocalDateTime sendDate;
}

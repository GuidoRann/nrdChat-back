package com.nrdChat.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatDTO {
    private Long chatId;
    private String name;
    private Boolean isGroup;
    private LocalDateTime lastMessageAt;
    private List<UserDTO> participants;
    private MessageDTO lastMessage;
}

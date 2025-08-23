package com.nrdChat.app.mapper;

import com.nrdChat.app.dtos.MessageDTO;
import com.nrdChat.app.dtos.UserDTO;
import com.nrdChat.app.model.MessageEntity;
import com.nrdChat.app.model.UserChat;
import org.springframework.security.core.userdetails.UserDetails;

public class DtoMapper {

    // ================== USER ==================
    public static UserDTO toUserDTO(UserChat user) {
        if (user == null) return null;

        return UserDTO.builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .name(user.getUsername())
                .role(user.getRole())
                .userState(user.getUserState())
                .build();
    }

    // Convierte UserDetails (Spring Security) → UserDTO
    public static UserDTO toUserDTO(UserDetails userDetails) {
        if (userDetails == null) return null;

        return UserDTO.builder()
                .email(userDetails.getUsername())
                .name(userDetails.getAuthorities().toString())
                .build();
    }

    public static UserChat toUserEntity(UserDTO dto) {
        if (dto == null) return null;

        return UserChat.builder()
                .userId(dto.getId())
                .email(dto.getEmail())
                .name(dto.getName())
                .build();
    }

    // ================== MESSAGE ==================
    public static MessageDTO toMessageDTO(MessageEntity entity) {
        if (entity == null) return null;

        return MessageDTO.builder()
                .id(entity.getMessageId())
                .content(entity.getContent())
                .sendDate(entity.getSendDate())
                .urlImg(entity.getUrlImg())
                .messageType(entity.getMessageType())
                .messageState(entity.getMessageState())
                .sender(toUserDTO(entity.getSender()))
                .receiver(toUserDTO(entity.getReceiver()))
                .build();
    }

    public static MessageEntity toMessageEntity(MessageDTO dto) {
        if (dto == null) return null;

        return MessageEntity.builder()
                .messageId(dto.getId())
                .content(dto.getContent())
                .sendDate(dto.getSendDate())
                .urlImg(dto.getUrlImg())
                .messageType(dto.getMessageType())
                .messageState(dto.getMessageState())
                .sender(toUserEntity(dto.getSender()))
                .receiver(toUserEntity(dto.getReceiver()))
                .build();
    }
}

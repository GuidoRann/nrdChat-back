package com.nrdChat.app.dtos;

import com.nrdChat.app.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestNotificationDTO {

    private NotificationType type;
    private String from;
    private String to;
    private Instant timestamp;
    private String message;
}

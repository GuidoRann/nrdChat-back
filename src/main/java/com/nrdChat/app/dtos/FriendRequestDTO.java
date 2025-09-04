package com.nrdChat.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Lightweight DTO for real-time friendship requests.
 * It does not replace FriendshipDTO (which handles lists and statuses),
 * but rather complements it for live events.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestDTO {
    private String senderEmail;
    private String receiverEmail;
    private String status;          // PENDING | ACCEPTED
}
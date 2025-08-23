package com.nrdChat.app.model;

import com.nrdChat.app.enums.MessageState;
import com.nrdChat.app.enums.MessageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class  MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;
    private String content;
    private LocalDateTime sendDate;
    private String urlImg;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;
    @Enumerated(EnumType.STRING)
    private MessageState messageState;

    @ManyToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "chatId")
    private ChatEntity chat;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "userId")
    private UserChat sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "userId")
    private UserChat receiver;
}

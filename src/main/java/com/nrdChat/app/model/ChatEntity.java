package com.nrdChat.app.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    private String name; // opcional: útil si después querés grupos con nombre

    private LocalDateTime createdAt;

    private LocalDateTime lastMessageAt; // se actualiza cada vez que alguien manda msg

    private Boolean isGroup; // false = chat privado (MSN style), true = grupo

    @ManyToMany
    @JoinTable(
            name = "chat_users",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserChat> participants = new HashSet<>();
}

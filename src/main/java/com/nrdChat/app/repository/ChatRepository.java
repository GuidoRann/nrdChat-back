package com.nrdChat.app.repository;

import com.nrdChat.app.model.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
    // Encontrar un chat privado entre 2 users
    @Query
    ("SELECT c FROM ChatEntity c JOIN c.participants p1 JOIN c.participants p2 " +
            "WHERE c.isGroup = false AND p1.userId = :userA AND p2.userId = :userB")
    Optional<ChatEntity> findPrivateChat(Long userA, Long userB);

    // Obtener todos los chats de un usuario (para la bandeja MSN)
    @Query("SELECT c FROM ChatEntity c JOIN c.participants p WHERE p.userId = :userId ORDER BY c.lastMessageAt DESC")
    List<ChatEntity> findAllByUser(Long userId);
}

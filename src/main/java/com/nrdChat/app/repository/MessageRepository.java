package com.nrdChat.app.repository;

import com.nrdChat.app.model.MessageEntity;
import com.nrdChat.app.model.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    @Query("SELECT m FROM MessageEntity m " +
            "WHERE (m.sender.email = :senderEmail AND m.receiver.email = :receiverEmail) " +
            "   OR (m.sender.email = :senderEmail AND m.receiver.email = :receiverEmail) " +
            "ORDER BY m.sendDate ASC")
    List<MessageEntity> findChatMessages(@Param("senderEmail") String email1, @Param("receiverEmail") String email2);

}

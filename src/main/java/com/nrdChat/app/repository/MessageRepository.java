package com.nrdChat.app.repository;

import com.nrdChat.app.model.MessageEntity;
import com.nrdChat.app.model.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    List<MessageEntity> findAllMessageByReceiverAndSender ( UserChat receiver, UserChat sender );
}

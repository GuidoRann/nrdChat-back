package com.nrdChat.app.repository;

import com.nrdChat.app.model.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserChat, Long> {
}

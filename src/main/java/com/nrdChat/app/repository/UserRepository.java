package com.nrdChat.app.repository;

import com.nrdChat.app.model.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserChat, Long> {

    Optional<UserChat> findByEmail(String email);
}

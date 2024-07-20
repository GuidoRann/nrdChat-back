package com.nrdChat.app.repository;

import com.nrdChat.app.model.FriendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<FriendEntity, Long> {
    FriendEntity findFriendByUserEmail (String email);
}

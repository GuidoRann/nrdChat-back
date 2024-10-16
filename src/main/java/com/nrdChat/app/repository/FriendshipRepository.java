package com.nrdChat.app.repository;

import com.nrdChat.app.model.FriendshipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<FriendshipEntity, Long> {

    List<FriendshipEntity> findAllFriendshipsByUserEmail( String userEmail );

    List<FriendshipEntity> findAllFriendshipsByFriendEmail( String friendEmail );

    Optional<FriendshipEntity> findFriendshipByUserEmailAndFriendEmail( String userEmail, String friendEmail );

    FriendshipEntity findFriendshipByFriendshipId(Long friendshipId);
}

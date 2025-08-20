package com.nrdChat.app.repository;

import com.nrdChat.app.enums.FriendshipState;
import com.nrdChat.app.model.FriendshipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<FriendshipEntity, Long> {

    // query for sentRequests or acceptedFriends depending on friendshipState
    List<FriendshipEntity> findByUserEmailAndFriendshipState( String userEmail, FriendshipState friendshipState );

    // query for pendingRequests
    List<FriendshipEntity> findByFriendEmailAndFriendshipState( String friendEmail, FriendshipState friendshipState );

    // query for specific friendship to accept
    Optional<FriendshipEntity> findFriendshipByUserEmailAndFriendEmail( String userEmail, String friendEmail );

}

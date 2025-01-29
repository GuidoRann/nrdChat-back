package com.nrdChat.app.repository;

import com.nrdChat.app.model.FriendshipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<FriendshipEntity, Long> {

    @Query("SELECT f FROM FriendshipEntity f " +
            "WHERE ( f.user.email = :userEmail ) " +
            "AND f.friendshipState = 'ACCEPTED'")
    List<FriendshipEntity> findAllAcceptedFriendships(@Param("userEmail") String userEmail);

    @Query("SELECT f FROM FriendshipEntity f " +
            "WHERE ( f.friend.email = :userEmail ) " +
            "AND f.friendshipState = 'PENDING'")
    List<FriendshipEntity> findAllPendingFriendships(@Param("userEmail") String userEmail);

    List<FriendshipEntity> findAllFriendshipsByUserEmail( String userEmail );

    List<FriendshipEntity> findAllFriendshipsByFriendEmail( String friendEmail );

    Optional<FriendshipEntity> findFriendshipByUserEmailAndFriendEmail( String userEmail, String friendEmail );

}

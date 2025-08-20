package com.nrdChat.app.service;

import com.nrdChat.app.dtos.FriendshipDTO;
import com.nrdChat.app.enums.FriendshipState;
import com.nrdChat.app.model.FriendshipEntity;
import com.nrdChat.app.model.UserChat;
import com.nrdChat.app.repository.FriendshipRepository;
import com.nrdChat.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class FriendshipManagementService implements IFriendshipManagementService {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public FriendshipDTO saveFriend( UserChat user, UserChat friend ) {
        FriendshipDTO resp = new FriendshipDTO();

        try {
            FriendshipEntity newFriend = FriendshipEntity.builder()
                    .user( user )
                    .friend( friend )
                    .friendshipState( FriendshipState.PENDING )
                    .build();
            FriendshipEntity savedFriend = friendshipRepository.save( newFriend );

            if ( savedFriend.getFriendshipId() > 0 ) {
                resp.setFriendship( savedFriend );
                resp.setStatusCode( 200 );
                resp.setMessage( "Friend added successfully" );
            }
        } catch ( Exception e ) {
            resp.setStatusCode( 500 );
            resp.setMessage( "Error occurred: " + e.getMessage() );
        }

        return resp;
    }

    @Override
    public FriendshipDTO getAllAcceptedFriends( String email ) {
        FriendshipDTO resp = new FriendshipDTO();

        try {
            List<FriendshipEntity> acceptedFriends = friendshipRepository.findByUserEmailAndFriendshipState( email, FriendshipState.ACCEPTED );

            resp.setAcceptedFriendsList( acceptedFriends );
            resp.setStatusCode( 200 );
            resp.setMessage( acceptedFriends.isEmpty()
                    ? "No accepted friendships found"
                    : "Successfully found accepted friendships" );

        } catch (Exception e) {
            resp.setStatusCode( 500 );
            resp.setMessage( "Error occurred: " + e.getMessage() );
        }

        return resp;
    }


    @Override
    public FriendshipDTO getAllPendingFriends( String email ) {
        FriendshipDTO resp = new FriendshipDTO();

        try {
            List<FriendshipEntity> pendingFriendList = friendshipRepository.findByFriendEmailAndFriendshipState( email, FriendshipState.PENDING );

            resp.setPendingFriendsList( pendingFriendList );
            resp.setStatusCode( 200 );

            if ( pendingFriendList.isEmpty() ) {
                resp.setMessage("No pending friendships found");
            } else {
                resp.setMessage("Successfully found pending friendships");
            }

        } catch ( Exception e ) {
            resp.setStatusCode( 500 );
            resp.setMessage( "Error occurred: " + e.getMessage() );
            return resp;
        }

        return resp;
    }

    @Override
    public FriendshipDTO getAllSentRequests( String email ) {
        FriendshipDTO resp = new FriendshipDTO();

        try {
            List<FriendshipEntity> sentList = friendshipRepository.findByUserEmailAndFriendshipState( email, FriendshipState.PENDING );
            if ( !sentList.isEmpty() ) {
                resp.setSentRequestsList( sentList );
                resp.setStatusCode( 200 );
                resp.setMessage("Successfully found sent requests");
            } else {
                resp.setSentRequestsList(Collections.emptyList());
                resp.setStatusCode( 200 );
                resp.setMessage("No sent requests found");
            }
        } catch (Exception e) {
            resp.setStatusCode( 500 );
            resp.setMessage( "Error occurred: " + e.getMessage() );
            return resp;
        }

        return resp;
    }

    @Override
    public FriendshipDTO acceptFriend( UserChat user, UserChat friend ) {
        FriendshipDTO resp = new FriendshipDTO();

        try {
            Optional<FriendshipEntity> friends = friendshipRepository.findFriendshipByUserEmailAndFriendEmail( user.getEmail(), friend.getEmail() );
            if ( friends.isPresent() ) {
                friends.get().setFriendshipState( FriendshipState.ACCEPTED );
                friendshipRepository.save( friends.get() );

                FriendshipEntity reciprocalFriendship = FriendshipEntity.builder()
                        .user( friend )          // Invertimos los roles
                        .friend( user )
                        .friendshipState( FriendshipState.ACCEPTED )
                        .build();

                friendshipRepository.save(reciprocalFriendship);

                resp.setStatusCode( 200 );
                resp.setMessage( "Friend accepted successfully" );
            } else {
                resp.setStatusCode( 404 );
                resp.setMessage( "Friend not found" );
            }
        } catch ( Exception e ) {
            resp.setStatusCode( 500 );
            resp.setMessage( "Error occurred: " + e.getMessage() );
        }
        return resp;
    }

    @Override
    public void deleteFriend(UserChat user, UserChat friend ) {
        FriendshipDTO resp = new FriendshipDTO();

        try {
            Optional<FriendshipEntity> friends = friendshipRepository.findFriendshipByUserEmailAndFriendEmail( user.getEmail(), friend.getEmail() );

            if ( friends.isPresent() ){
                friendshipRepository.delete( friends.get() );
                resp.setStatusCode( 200 );
                resp.setMessage( "Friend deleted successfully" );
            } else {
                resp.setStatusCode( 404 );
                resp.setMessage( "Friend not found" );
            }

        } catch ( Exception e ) {
            resp.setStatusCode( 500 );
            resp.setMessage( "Error occurred: " + e.getMessage() );
        }

    }
}

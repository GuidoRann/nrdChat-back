package com.nrdChat.app.service;

import com.nrdChat.app.dtos.FriendshipDTO;
import com.nrdChat.app.enums.FriendshipState;
import com.nrdChat.app.model.FriendshipEntity;
import com.nrdChat.app.model.UserChat;
import com.nrdChat.app.repository.FriendshipRepository;
import com.nrdChat.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public FriendshipDTO getFriendList( String email ) {
        FriendshipDTO resp = new FriendshipDTO();

        try {
            List<FriendshipEntity> friendList = friendshipRepository.findAllFriendshipsByFriendEmail( email );

            if ( !friendList.isEmpty() ) {
                resp.setFriendshipList(friendList);
                resp.setStatusCode( 200 );
                resp.setMessage("Successfully found friendships");
            } else {
                resp.setStatusCode( 404 );
                resp.setMessage("No friendships found");
            }

        return resp;

        } catch (Exception e) {
            resp.setStatusCode( 500 );
            resp.setMessage( "Error occurred: " + e.getMessage() );
            return resp;
        }

    }

    @Override
    public FriendshipDTO getFriendRequests( String email ) {
        FriendshipDTO resp = new FriendshipDTO();

        try {
            List<FriendshipEntity> friendRequests = friendshipRepository.findAllFriendshipsByFriendEmail( email );

            if ( !friendRequests.isEmpty() ) {
                resp.setFriendshipRequests(friendRequests);
                resp.setStatusCode( 200 );
                resp.setMessage("Successfully found friend requests");
            } else {
                resp.setStatusCode( 404 );
                resp.setMessage("No friend requests found");
            }

        return resp;

        } catch (Exception e) {
            resp.setStatusCode( 500 );
            resp.setMessage( "Error occurred: " + e.getMessage() );
            return resp;
        }

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
                        .user(friend)          // Invertimos los roles
                        .friend(user)
                        .friendshipState(FriendshipState.ACCEPTED)
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
    public FriendshipDTO deleteFriend( UserChat user, UserChat friend ) {
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

        return resp;
    }
}

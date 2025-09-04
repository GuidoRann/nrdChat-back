package com.nrdChat.app.service;

import com.nrdChat.app.dtos.FriendshipDTO;
import com.nrdChat.app.enums.FriendshipState;
import com.nrdChat.app.mapper.DtoMapper;
import com.nrdChat.app.model.FriendshipEntity;
import com.nrdChat.app.model.UserChat;
import com.nrdChat.app.repository.FriendshipRepository;
import com.nrdChat.app.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class FriendshipManagementService implements IFriendshipManagementService {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public FriendshipDTO saveFriend( UserChat user, UserChat friend ) {
        FriendshipDTO resp = new FriendshipDTO();

            UserChat userDb = userRepository.findByEmail( user.getEmail() )
                    .orElseThrow(() -> new RuntimeException("User not found: " + user.getEmail()));

            UserChat friendDb = userRepository.findByEmail( friend.getEmail() )
                    .orElseThrow(() -> new RuntimeException("Friend not found: " + friend.getEmail()));

        try {
            FriendshipEntity newFriend = FriendshipEntity.builder()
                    .user( userDb )
                    .friend( friendDb )
                    .friendshipState( FriendshipState.PENDING )
                    .build();

            FriendshipEntity savedFriend = friendshipRepository.save( newFriend );

            if ( savedFriend.getFriendshipId() > 0 ) {
                FriendshipDTO friendshipDTO = DtoMapper.toFriendshipDTO( savedFriend );

                resp.setFriendship( friendshipDTO );
                resp.setStatusCode( 200 );
                resp.setMessage( "Friend added successfully" );

                messagingTemplate.convertAndSendToUser(
                        friendDb.getEmail(),
                        "/queue/friend-requests",
                        friendshipDTO
                );
            }
        } catch ( Exception e ) {
            resp = FriendshipDTO.builder()
                    .statusCode( 500 )
                    .message( "Error occurred: " + e.getMessage() )
                    .build();
        }

        return resp;
    }

    @Override
    public FriendshipDTO getAllAcceptedFriends( String email ) {
        FriendshipDTO resp;

        try {
            List<FriendshipEntity> acceptedFriends = friendshipRepository.findByUserEmailAndFriendshipState( email, FriendshipState.ACCEPTED );

            List<FriendshipDTO> acceptedDtoList = acceptedFriends.stream()
                    .map( DtoMapper::toFriendshipDTO )
                    .toList();

            resp = FriendshipDTO.builder()
                    .AcceptedFriendsList( acceptedDtoList )
                    .statusCode( 200 )
                    .message( acceptedDtoList.isEmpty()
                           ? "No accepted friendships found"
                           : "Successfully found accepted friendships" )
                    .build();

        } catch (Exception e) {
            resp = FriendshipDTO.builder()
                    .statusCode( 500 )
                    .message( "Error occurred: " + e.getMessage() )
                    .build();
        }

        return resp;
    }


    @Override
    public FriendshipDTO getAllPendingFriends( String email ) {
        FriendshipDTO resp;

        try {
            List<FriendshipEntity> pendingFriendList = friendshipRepository.findByFriendEmailAndFriendshipState( email, FriendshipState.PENDING );

            List<FriendshipDTO> pendingDtoList = pendingFriendList.stream()
                    .map( DtoMapper::toFriendshipDTO )
                    .toList();

            resp = FriendshipDTO.builder()
                    .PendingFriendsList( pendingDtoList )
                    .statusCode( 200 )
                    .message( pendingDtoList.isEmpty()
                            ? "No pending friendships found"
                            : "Successfully found pending friendships" )
                    .build();

        } catch ( Exception e ) {
            resp = FriendshipDTO.builder()
                    .statusCode( 500 )
                    .message( "Error occurred: " + e.getMessage() )
                    .build();
        }
        return resp;
    }

    @Override
    public FriendshipDTO getAllSentRequests( String email ) {
        FriendshipDTO resp;

        try {
            List<FriendshipEntity> sentList = friendshipRepository.findByUserEmailAndFriendshipState( email, FriendshipState.PENDING );

            List<FriendshipDTO> sentDtoList = sentList == null
                    ? Collections.emptyList()
                    : sentList.stream()
                    .map( DtoMapper::toFriendshipDTO )
                    .toList();

            resp = FriendshipDTO.builder()
                    .SentRequestsList( sentDtoList )
                    .statusCode( 200 )
                    .message( sentDtoList.isEmpty()
                            ? "No sent requests found"
                            : "Successfully found sent requests" )
                    .build();

        } catch (Exception e) {
            resp = FriendshipDTO.builder()
                    .statusCode( 500 )
                    .message( "Error occurred: " + e.getMessage() )
                    .build();
        }
        return resp;
    }

    @Override
    public FriendshipDTO acceptFriend( UserChat user, UserChat friend ) {
        FriendshipDTO resp = new FriendshipDTO();

        UserChat userDb = userRepository.findByEmail( user.getEmail() )
                .orElseThrow(() -> new RuntimeException("User not found: " + user.getEmail()));

        UserChat friendDb = userRepository.findByEmail( friend.getEmail() )
                .orElseThrow(() -> new RuntimeException("Friend not found: " + friend.getEmail()));

        try {
            Optional<FriendshipEntity> friends = friendshipRepository.findFriendshipByUserEmailAndFriendEmail( userDb.getEmail(), friendDb.getEmail() );

            if ( friends.isPresent() ) {
                friends.get().setFriendshipState( FriendshipState.ACCEPTED );
                friendshipRepository.save( friends.get() );

                FriendshipEntity reciprocalFriendship = FriendshipEntity.builder()
                        .user( friendDb )          // Invertimos los roles
                        .friend( userDb )
                        .friendshipState( FriendshipState.ACCEPTED )
                        .build();

                friendshipRepository.save( reciprocalFriendship );

                messagingTemplate.convertAndSendToUser(
                        friendDb.getEmail(),  // el que originalmente envió la request
                        "/queue/friend-requests-updates",
                        DtoMapper.toFriendshipDTO( friends.get() )
                );

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
    public void deleteFriend( UserChat user, UserChat friend ) {
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

package com.nrdChat.app.controller;

import com.nrdChat.app.dtos.FriendRequestDTO;
import com.nrdChat.app.dtos.FriendRequestNotificationDTO;
import com.nrdChat.app.dtos.FriendshipDTO;
import com.nrdChat.app.dtos.UserDTO;
import com.nrdChat.app.enums.NotificationType;
import com.nrdChat.app.mapper.DtoMapper;
import com.nrdChat.app.model.UserChat;
import com.nrdChat.app.model.UserPrincipal;
import com.nrdChat.app.service.FriendshipManagementService;
import com.nrdChat.app.service.UserManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Instant;

@Slf4j
@RestController
public class FriendshipController {

    @Autowired
    FriendshipManagementService friendshipManagementService;

    @Autowired
    UserManagementService userManagementService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/adminuser/add-friend")
    public ResponseEntity<FriendshipDTO> saveFriend (@RequestBody FriendshipDTO request) {
        return ResponseEntity.ok( friendshipManagementService.saveFriend( DtoMapper.toUserEntity( request.getUser() ), DtoMapper.toUserEntity( request.getFriend() ) ));
    }

    @GetMapping("/auth/get-accepted-friends")
    public ResponseEntity<FriendshipDTO> acceptedFriends() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        FriendshipDTO resp = friendshipManagementService.getAllAcceptedFriends( email );
        return ResponseEntity.status(resp.getStatusCode()).body( resp );
    }

    @GetMapping("/auth/get-pending-friends")
    public ResponseEntity<FriendshipDTO> friendRequests() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        FriendshipDTO resp = friendshipManagementService.getAllPendingFriends( email );
        return ResponseEntity.status( resp.getStatusCode() ).body( resp );
    }

    @GetMapping("/auth/get-sent-requests")
    public ResponseEntity<FriendshipDTO> sentRequests() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        FriendshipDTO resp = friendshipManagementService.getAllSentRequests( email );
        return ResponseEntity.status( resp.getStatusCode() ).body( resp );
    }


    @PutMapping("/auth/accept-friend")
    public ResponseEntity<FriendshipDTO> acceptFriend( @RequestBody FriendshipDTO request ) {
        return ResponseEntity.ok(friendshipManagementService.acceptFriend( DtoMapper.toUserEntity( request.getUser() ), DtoMapper.toUserEntity( request.getFriend() ) ));
    }

    // ---------------- WebSocket ----------------

    @MessageMapping("/friends.sendRequest")
    public void sendFriendRequest(FriendRequestDTO request, java.security.Principal principal) {
        var up = (UserPrincipal) principal;
        var sender = up.getUserChat();
        var receiver = DtoMapper.toUserEntity( userManagementService.getUserByEmail( request.getReceiverEmail() ) );

        var resp = friendshipManagementService.saveFriend( sender, receiver );

        FriendRequestNotificationDTO notification = FriendRequestNotificationDTO.builder()
                .type( NotificationType.REQUEST_SENT )
                .from( sender.getEmail() )
                .to( receiver.getEmail() )
                .timestamp( Instant.now() )
                .message( sender.getEmail() + " te envió una solicitud de amistad" )
                .build();

        if ( resp.getStatusCode() == 200 ) {
            simpMessagingTemplate.convertAndSendToUser(
                    receiver.getEmail(),
                    "/queue/friend-requests",
                    notification
            );
        }
    }

    @MessageMapping("/friend.acceptRequest")
    public void acceptRequest(@Payload FriendRequestDTO request, Principal principal) {
        var up = (UserPrincipal) principal;
        UserChat receiver = up.getUserChat();
        UserChat sender = DtoMapper.toUserEntity( userManagementService.getUserByEmail( request.getSenderEmail() ) );

        FriendshipDTO updated = friendshipManagementService.acceptFriend(
                sender,
                receiver
        );

        FriendRequestNotificationDTO notif = FriendRequestNotificationDTO.builder()
                .type( NotificationType.REQUEST_ACCEPTED )
                .from( receiver.getEmail() )
                .to( sender.getEmail() )
                .timestamp( Instant.now() )
                .message( receiver.getEmail() + " aceptó tu solicitud de amistad" )
                .build();

        // Notificar a ambos
        simpMessagingTemplate.convertAndSendToUser(
                sender.getEmail(),
                "/queue/friend-updates",
                updated
        );
        simpMessagingTemplate.convertAndSendToUser(
                receiver.getEmail(),
                "/queue/friend-updates",
                updated
        );
    }

    @MessageMapping("/friend.rejectRequest")
    public void rejectRequest(@Payload FriendRequestDTO request, Principal principal) {
        var up = ( UserPrincipal ) principal;
        UserChat receiver = up.getUserChat();
        UserChat sender = DtoMapper.toUserEntity( userManagementService.getUserByEmail( request.getSenderEmail() ) );

        friendshipManagementService.deleteFriend(
                sender,
                receiver
        );

        simpMessagingTemplate.convertAndSendToUser(
                sender.getEmail(),
                "/queue/friend-updates",
                "Your friend request was rejected by " + receiver.getEmail()
        );
    }

}

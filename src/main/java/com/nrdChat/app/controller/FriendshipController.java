package com.nrdChat.app.controller;

import com.nrdChat.app.dtos.FriendshipDTO;
import com.nrdChat.app.service.FriendshipManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
public class FriendshipController {

    @Autowired
    FriendshipManagementService friendshipManagementService;

    @PostMapping("/adminuser/add-friend")
    public ResponseEntity<FriendshipDTO> saveFriend (@RequestBody FriendshipDTO request) {
        return ResponseEntity.ok(friendshipManagementService.saveFriend(request.getUser(), request.getFriend()));
    }

    @GetMapping("/auth/get-accepted-friends")
    public ResponseEntity<FriendshipDTO> acceptedFriends() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        FriendshipDTO resp = friendshipManagementService.getAllAcceptedFriends(email);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
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
        return ResponseEntity.ok(friendshipManagementService.acceptFriend( request.getUser(), request.getFriend() ));
    }

    @DeleteMapping("/auth/delete-friend")
    public ResponseEntity<Void> deleteFriend(@RequestBody FriendshipDTO request) {
        friendshipManagementService.deleteFriend(request.getUser(), request.getFriend());
        return ResponseEntity.noContent().build(); // 204 No Content
    }

}

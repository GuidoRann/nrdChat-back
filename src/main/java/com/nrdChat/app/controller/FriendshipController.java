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


//    TODO: make this endpoint adminuser instead of auth
    @GetMapping("/auth/get-friendlist")
    public ResponseEntity<FriendshipDTO> friendList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        FriendshipDTO resp = friendshipManagementService.getFriendList(email);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @GetMapping("/auth/get-friend-requests")
    public ResponseEntity<FriendshipDTO> friendRequests() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        FriendshipDTO resp = friendshipManagementService.getFriendRequests(email);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @PutMapping("/auth/accept-friend")
    public ResponseEntity<FriendshipDTO> acceptFriend( @RequestBody FriendshipDTO request ) {
        return ResponseEntity.ok(friendshipManagementService.acceptFriend( request.getUser(), request.getFriend() ));
    }

    @DeleteMapping("/auth/delete-friend")
    public ResponseEntity<FriendshipDTO> deleteFriend( @RequestBody FriendshipDTO request ) {
        return ResponseEntity.ok(friendshipManagementService.deleteFriend( request.getUser(), request.getFriend() ));
    }

}

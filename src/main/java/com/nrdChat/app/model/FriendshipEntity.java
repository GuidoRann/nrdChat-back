package com.nrdChat.app.model;

import com.nrdChat.app.enums.FriendshipState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendshipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendshipId;

    @ManyToOne
    @JoinColumn(name = "user_email", referencedColumnName = "email")
    private UserChat user;

    @ManyToOne
    @JoinColumn(name = "friend_email", referencedColumnName = "email")
    private UserChat friend;

    @Enumerated(EnumType.STRING)
    private FriendshipState friendshipState;
}

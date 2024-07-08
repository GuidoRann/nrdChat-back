package com.nrdChat.app.model;

import com.nrdChat.app.enums.FriendState;
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
public class FriendEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendId;

    @ManyToOne
    @JoinColumn(name = "user_email", referencedColumnName = "email")
    private UserChat user;

    @ManyToOne
    @JoinColumn(name = "friend_user_id", referencedColumnName = "userId")
    private UserChat friend;

    @Enumerated(EnumType.STRING)
    private FriendState friendState;
}

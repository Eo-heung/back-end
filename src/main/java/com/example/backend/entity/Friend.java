package com.example.backend.entity;

import com.example.backend.dto.FriendDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "FRIEND")
public class Friend {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    private boolean status;

    @Column(name = "from_friend_user")
    private String fromUser;

    @Column(name = "to_friend_user")
    private String toUser;


    public FriendDTO EntityToDTO() {
        return FriendDTO.builder()
                .id(this.id)
                .status(this.status)
                .fromUser(this.fromUser)
                .toUser(this.toUser)
                .build();
    }
}

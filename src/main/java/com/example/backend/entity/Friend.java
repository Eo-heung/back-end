package com.example.backend.entity;

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
    private Long id;

    @Column(name = "status")
    private boolean status;

    @Column(name = "from_friend_user")
    private String fromUser;

    @Column(name = "to_friend_user")
    private String toUser;


}

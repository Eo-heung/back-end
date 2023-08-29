package com.example.backend.dto;

import com.example.backend.entity.Friend;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendDTO {

    private Long id;
    private boolean status;
    private String fromUser;
    private String toUser;

    public Friend EntityToDTO() {
        return Friend.builder()
                .id(this.id)
                .status(this.status)
                .fromUser(this.fromUser)
                .toUser(this.toUser)
                .build();
    }

}

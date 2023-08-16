package com.example.backend.dto;

import com.example.backend.entity.ProfileImage;
import com.example.backend.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileImageDTO {

    private Long id;

    private String userId;

    private byte[] fileData;

    private LocalDateTime createDatetime;

    private LocalDateTime updateDatetime;


    public ProfileImage DTOToEntity() {
        return ProfileImage.builder()
                .id(this.id)
                .userId(
                        User.builder()
                                .userId(this.userId)
                                .build()
                )
                .fileData(this.fileData)
                .createDatetime(LocalDateTime.now())
                .updateDatetime(this.updateDatetime)
                .build();
    }


}

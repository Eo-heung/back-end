package com.example.backend.entity;

import com.example.backend.dto.ProfileImageDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "PICTURE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileImage {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @Lob
    @Column(name = "profile")
    private byte[] fileData;

    @Column(name = "create_datetime")
    private LocalDateTime createDatetime;

    @Column(name = "update_datetime")
    private LocalDateTime updateDatetime;

    public ProfileImageDTO EntityToDTO() {
        return ProfileImageDTO.builder()
                .id(this.id)
                .userId(this.userId.getUserId())
                .fileData(this.fileData)
                .createDatetime(this.createDatetime)
                .updateDatetime(this.updateDatetime)
                .build();
    }

}

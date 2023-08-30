package com.example.backend.entity;

import com.example.backend.dto.MoimPictureDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "MOIM_PICTURE")
public class MoimPicture {

    @Id
    @Column(name = "pic_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int picId; //사진Id

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId; //모임장Id

    @OneToOne
    @JoinColumn(name = "moim_id")
    private Moim moimId; //모임Id

    @Lob
    @Column(name = "moim_pic", columnDefinition = "MEDIUMBLOB")
    private byte[] moimPic; //사진저장

    @Column(name = "create_pic", updatable = false)
    private LocalDateTime createPic; //등록일

    @Column(name = "update_pic")
    private LocalDateTime updatePic; //수정일



    @PrePersist
    public void prePersist() {
        this.createPic = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatePic = LocalDateTime.now();
    }



    public MoimPictureDTO EntityToDTO() {
        return MoimPictureDTO.builder()
                .picId(this.picId)
                .userId(this.userId.getUserId())
                .moimId(this.moimId.getMoimId())
                .createPic(this.createPic)
                .updatePic(this.updatePic)
                .build();
    }



}
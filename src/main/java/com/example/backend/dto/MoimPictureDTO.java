package com.example.backend.dto;

import com.example.backend.entity.Moim;
import com.example.backend.entity.MoimPicture;
import com.example.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoimPictureDTO {

    private int picId;
    private String userId;
    private int moimId;
    private LocalDateTime createPic;
    private LocalDateTime updatePic;



    public MoimPicture DTOToEntity() {
        return MoimPicture.builder()
                .picId(this.picId)
                .userId(
                        User.builder()
                                .userId(this.userId)
                                .build()
                )
                .moimId(
                        Moim.builder()
                                .moimId(this.moimId)
                                .build()
                )
                .createPic(this.createPic)
                .updatePic(this.updatePic)
                .build();
    }
}
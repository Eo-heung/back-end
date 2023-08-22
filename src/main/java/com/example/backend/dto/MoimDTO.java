package com.example.backend.dto;

import com.example.backend.entity.Moim;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoimDTO {

    private int moimId;
    private String userId;
    private String moimNickname;
    private String moimCategory;
    private LocalDateTime moimRegdate;
    private String moimTitle;
    private String moimContent;
    private String onOff;
    private String moimAddr;
    private int maxMoimUser;
    private int currentMoimUser;
    private String isDelete;
    private String isEnd;



    public Moim DTOToEntity() {
        return Moim.builder()
                .moimId(this.moimId)
                .userId(
                        User.builder()
                                .userId(this.userId)
                                .build()
                )
                .moimNickname(this.moimNickname)
                .moimCategory(this.moimCategory)
                .moimRegdate(this.moimRegdate)
                .moimTitle(this.moimTitle)
                .moimContent(this.moimContent)
                .onOff(this.onOff)
                .moimAddr(this.moimAddr)
                .maxMoimUser(this.maxMoimUser)
                .currentMoimUser(this.currentMoimUser)
                .isDelete(this.isDelete)
                .isEnd(this.isEnd)
                .build();
    }


}
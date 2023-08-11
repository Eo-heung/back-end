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
    private String moimCategory;
    private LocalDateTime moimRegdate;
    private String moimTitle;
    private String moimContent;
    private String onOff;
    private String moimAddr1;
    private String moimAddr2;
    private String moimAddr3;
    private String limitAge;
    private String maxMoimUser;
    private String cost;
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
                .moimCategory(this.moimCategory)
                .moimRegdate(LocalDateTime.now())
                .moimTitle(this.moimTitle)
                .moimContent(this.moimContent)
                .onOff(this.onOff)
                .moimAddr1(this.moimAddr1)
                .moimAddr2(this.moimAddr2)
                .moimAddr3(this.moimAddr3)
                .limitAge(this.limitAge)
                .maxMoimUser(this.maxMoimUser)
                .cost(this.cost)
                .isDelete(this.isDelete)
                .isEnd(this.isEnd)
                .build();
    }
}
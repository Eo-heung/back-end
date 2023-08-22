package com.example.backend.entity;

import com.example.backend.dto.MoimDTO;
import com.example.backend.dto.UserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "MOIM")
public class Moim {

    //모임 Moim
    @Id
    @Column(name = "moim_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int moimId; //모임Id

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId; //모임장Id

    @Column(name = "moim_nickname")
    private String moimNickname; //모임장닉네임

    @Column(name = "moim_category")
    private String moimCategory;

    @Column(name = "moim_regdate",updatable = false)
    private LocalDateTime moimRegdate; //작성일

    @Column(name = "moim_title")
    private String moimTitle; //모임명

    @Column(name = "moim_content")
    private String moimContent; //모임소개

    @Column(name = "on_off") // 고침
    private String onOff; //온오프라인

    @Column(name = "moim_addr")
    private String moimAddr; //모임 상세 입력주소

    @Column(name = "max_moim_user")
    private int maxMoimUser; //정원(최대 50명)

    @Column(name = "current_moim_user")
    private int currentMoimUser; //현재 모임 가입자

    @Column(name = "is_delete")
    private String isDelete; //삭제여부

    @Column(name = "is_end")
    private String isEnd; //종료여부

    public MoimDTO EntityToDTO() {
        return MoimDTO.builder()
                .moimId(this.moimId)
                .userId(this.userId.getUserId())
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
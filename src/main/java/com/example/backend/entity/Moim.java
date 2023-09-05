package com.example.backend.entity;

import com.example.backend.dto.MoimDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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

    @OneToOne(mappedBy = "moimId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private MoimPicture moimPicture;


    @PrePersist
    public void onPrePersist() {
        this.currentMoimUser = 1;
    }


    // 모임 가입자 수 증가
    public void incrementCurrentMoimUser() {
        if (this.currentMoimUser + 1 > this.maxMoimUser) {
            throw new IllegalStateException("모임 정원을 초과하여 가입할 수 없습니다.");
        }
        this.currentMoimUser += 1;
    }

    // 모임 가입자 수 감소
    public void decrementCurrentMoimUser() {
        if (this.currentMoimUser - 1 < 0) {
            throw new IllegalStateException("모임 가입자 수가 이미 0입니다.");
        }
        this.currentMoimUser -= 1;
    }



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
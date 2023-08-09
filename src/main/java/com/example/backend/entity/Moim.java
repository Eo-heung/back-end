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

    @Column(name = "moim_regdate")
    private LocalDateTime moimRegdate = LocalDateTime.now(); //작성일시

    @Column(name = "moim_title")
    private String moimTitle; //모임명

    @Column(name = "moim_content")
    private String moimContent; //모임소개 ////ㅇㅇㅇㅇ

    @Column(name = "on_off") // 고침
    private String onOff; //온오프라인

    @Column(name = "moim_addr1")
    private String moimAddr1; //모임 우편주소

    @Column(name = "moim_addr2")
    private String moimAddr2; //모임 기본주소

    @Column(name = "moim_addr3")
    private String moimAddr3; //모임 상세 입력주소

    @Column(name = "limit_age") // 고침
    private String limitAge; //나이제한

    @Column(name = "max_moim_user")
    private String maxMoimUser; //정원 ////ㅇㅇㅇㅇ 최대 50명

    @Column(name = "cost")
    private String cost; //참가비

    @Column(name = "is_delete")
    private String isDelete; //삭제여부

    @Column(name = "is_end")
    private String isEnd; //종료여부

    public MoimDTO EntityToDTO() {
        return MoimDTO.builder()
                .moimId(this.moimId)
                .userId(this.userId.getUserId())
                .moimRegdate(this.moimRegdate)
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

package com.example.backend.entity;

import com.example.backend.dto.MoimRegistrationDTO;
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
@Table(name = "MOIM_REGISTRATION")
public class MoimRegistration {
    @Id
    @Column(name = "moim_reg_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int moimRegId;

    @ManyToOne
    @JoinColumn(name = "moim_id")
    private Moim moim; //모임Id

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; //가입자 Id

    @Lob
    @Column(name = "moim_profile", columnDefinition = "MEDIUMBLOB")
    private byte[] moimProfile; //모임에서만 사용할 프로필 사진

    @Column(name = "create_moim_profile")
    private LocalDateTime createMoimProfile; //사진 저장일

    @Column(name = "update_moim_profile")
    private LocalDateTime updateMoimProfile; //사진 수정일

    @Enumerated(EnumType.STRING)
    @Column(name = "reg_status")
    private RegStatus regStatus; // 가입 상태 (승인, 대기, 거절, 취소, 탈퇴)

    @Column(name = "application_date")
    private LocalDateTime applicationDate; // 신청일

    @Column(name = "subscribe_date")
    private LocalDateTime subscribeDate; // 가입일

    @Column(name = "quit_date")
    private LocalDateTime quitDate; // 탈퇴일

    //알림 전송용 코드
    @Column(name = "Reg_ALARM")
    private int regAlarm; //알림 읽음 여부 (0: 읽지 않음, 1: 읽음)

    public enum RegStatus {
        APPROVED, // 승인(모임장)
        WAITING,  // 대기(모임장)
        REJECTED,  // 거절(모임장)
        CANCELED, // 취소(신청자)
        QUIT //탈퇴(신청자)
    }


    public String applicantUserNickname() {
        if (this.user != null) {
            return this.user.getUserNickname();
        }
        return null;
    }

    public String applicantUserAddr() {
        if (this.user != null) {
            return this.user.getUserAddr3();
        }
        return null;
    }

    public MoimRegistrationDTO EntityToDTO() {
        return MoimRegistrationDTO.builder()
                .moimRegId(this.moimRegId)
                .moim(this.moim)
                .user(this.user)
                .moimProfile(this.moimProfile)
                .regStatus(this.regStatus)
                .applicationDate(this.applicationDate)
                .subscribeDate(this.subscribeDate)
                .regAlarm(this.regAlarm)
                .build();
    }


    public MoimRegistrationDTO toDTO() {
        return MoimRegistrationDTO.builder()
                .moimRegId(this.moimRegId)
                .moimProfile(this.moimProfile)
                .regStatus(this.regStatus)
                .applicationDate(this.applicationDate)
                .applicantUserNickname(this.user.getUserNickname())
                .applicantUserAddr(this.user.getUserAddr3())
                .build();
    }

}

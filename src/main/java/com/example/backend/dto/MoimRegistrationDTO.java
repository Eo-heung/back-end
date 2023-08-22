package com.example.backend.dto;

import com.example.backend.entity.Moim;
import com.example.backend.entity.MoimRegistration;
import com.example.backend.entity.ProfileImage;
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
public class MoimRegistrationDTO {
    private int moimRegId;
    private Moim moim;
    private User user;
    private byte[] moimProfile;
    private LocalDateTime createMoimProfile;
    private LocalDateTime updateMoimProfile;
    private MoimRegistration.RegStatus regStatus;
    private LocalDateTime applicationDate;
    private LocalDateTime subscribeDate;
    private int regAlarm;
    private String applicantUserNickname;
    private String applicantUserAddr;
    private String applicantUserId;

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
                .applicantUserNickname(this.getUser().getUserNickname())
                .applicantUserAddr(this.getUser().getUserAddr3())
                .build();
    }


}

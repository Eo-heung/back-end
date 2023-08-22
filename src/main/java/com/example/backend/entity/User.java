package com.example.backend.entity;

import com.example.backend.dto.UserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "USER")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
        @Id
        @Column(name = "user_id", nullable = false)
        private String userId;

        @Column(name = "user_pw")
        private String userPw;

        @Column(name = "user_name")
        private String userName;

        @Column(name = "user_nickname")
        private String userNickname;

        @Column(name = "user_email")
        private String userEmail;

        @Column(name = "user_gender")
        private Integer userGender;

        @Column(name = "user_birth")
        private String userBirth;

        @Column(name = "user_tel")
        private String userTel;

        @Column(name = "user_addr1")
        private String userAddr1;

        @Column(name = "user_addr2")
        private String userAddr2;

        @Column(name = "user_addr3")
        private String userAddr3;

        @Column(name = "user_regdate")
        private LocalDateTime userRegdate;

        @Column(name = "user_update")
        private LocalDateTime userUpdate;

        @Column(name = "user_recommend")
        private String userRecommend;

        @Column(name = "user_manner_score")
        private Double userMannerScore;

        @Column(name = "user_authentication")
        private Boolean userAuthentication;

        @Column(name = "user_hobby1")
        private Integer userHobby1;

        @Column(name = "user_hobby2")
        private Integer userHobby2;

        @Column(name = "user_hobby3")
        private Integer userHobby3;

        @Column(name = "user_status_message")
        private String userStatusMessage;

        @Column(name = "create_datetime")
        private LocalDateTime createDatetime;

        @Column(name = "update_datetime")
        private LocalDateTime updateDatetime;

        @Column(name = "totalGam")
        private Long totalGam;

    public UserDTO EntityToDTO() {
            return UserDTO.builder()
                .userId(this.userId)
                .userPw(this.userPw)
                .userName(this.userName)
                .userNickname(this.userNickname)
                .userEmail(this.userEmail)
                .userGender(this.userGender)
                .userBirth(this.userBirth)
                .userTel(this.userTel)
                .userAddr1(this.userAddr1)
                .userAddr2(this.userAddr2)
                .userAddr3(this.userAddr3)
                .userRegdate(this.userRegdate)
                .userUpdate(this.userUpdate)
                .userRecommend(this.userRecommend)
                .userMannerScore(this.userMannerScore)
                .userAuthentication(this.userAuthentication)
                .userHobby1(this.userHobby1)
                .userHobby2(this.userHobby2)
                .userHobby3(this.userHobby3)
                .userStatusMessage(this.userStatusMessage)
                .createDatetime(this.createDatetime)
                .updateDatetime(this.updateDatetime)
                .totalGam(this.totalGam)
                .build();
    }
}


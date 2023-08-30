package com.example.backend.dto;

import com.example.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private String userId;

    private String userPw;

    private String userName;

    private String userNickname;

    private String userEmail;

    private Integer userGender;

    private String userBirth;

    private String userTel;

    private String userAddr1;

    private String userAddr2;

    private String userAddr3;

    private LocalDateTime userRegdate;

    private LocalDateTime userUpdate;

    private String userRecommend;

    private Double userMannerScore;

    private Boolean userAuthentication;

    private Integer userHobby1;

    private Integer userHobby2;

    private Integer userHobby3;

    private String userStatusMessage;

    private LocalDateTime lastHeartbeat;

    private String token;

    private Long  totalGam;
    private Integer age;
    private Boolean online;

    public User DTOToEntity() {
        return User.builder()
                .userId(userId)
                .userPw(userPw)
                .userName(userName)
                .userNickname(userNickname)
                .userEmail(userEmail)
                .userGender(userGender)
                .userBirth(userBirth)
                .userTel(userTel)
                .userAddr1(userAddr1)
                .userAddr2(userAddr2)
                .userAddr3(userAddr3)
                .userRegdate(LocalDateTime.now())
                .userUpdate(userUpdate)
                .userRecommend(userRecommend)
                .userMannerScore(userMannerScore)
                .userAuthentication(userAuthentication)
                .userHobby1(userHobby1)
                .userHobby2(userHobby2)
                .userHobby3(userHobby3)
                .userStatusMessage(userStatusMessage)
                .lastHeartbeat(lastHeartbeat)
                .totalGam(totalGam)
                .online(online)
                .build();
    }

}

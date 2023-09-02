package com.example.backend.entity;

import com.example.backend.dto.AppBoardDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "APP_BOARD")
public class AppBoard {

    @Id
    @Column(name = "app_board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int appBoardId; //약속 게시글 Id

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; //약속 게시자

    @ManyToOne
    @JoinColumn(name = "moim_id")
    private Moim moim; //모임Id

    @Column(name = "app_regdate")
    private LocalDateTime appRegdate;

    @Enumerated(EnumType.STRING)
    @Column(name = "app_type")
    private AppType appType;  //온라인, 오프라인

    @Column(name = "app_title")
    private String appTitle;

    @Column(name = "app_content")
    private String appContent;

    @Column(name = "app_location")
    private String appLocation;

    @Column(name = "app_start")
    private LocalDateTime appStart;

    @Column(name = "app_end")
    private LocalDateTime appEnd;

    @Column(name = "max_app_user")
    private int maxAppUser; //정원(최대 50명)

    @Column(name = "current_moim_user")
    private int currentAppUser; //현재 약속 신청 확정 인원

    public enum AppType {
        ONLINE, OFFLINE
    }

    @PrePersist
    public void onPrePersist() {
        this.appRegdate = LocalDateTime.now();
        this.currentAppUser = 1;
    }

    public AppBoardDTO EntityToDTO(String userName) {
        return AppBoardDTO.builder()
                .appBoardId(this.appBoardId)
                .user(this.user.getUserId())
                .moim(this.moim.getMoimId())
                .appRegdate(this.appRegdate)
                .appType(this.appType)
                .appTitle(this.appTitle)
                .appContent(this.appContent)
                .appLocation(this.appLocation)
                .appStart(this.appStart)
                .appEnd(this.appEnd)
                .maxAppUser(this.maxAppUser)
                .currentAppUser(this.currentAppUser)
                .userName(userName)
                .build();
    }






}

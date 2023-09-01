package com.example.backend.dto;

import com.example.backend.entity.AppBoard;
import com.example.backend.entity.Board;
import com.example.backend.entity.Moim;
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
public class AppBoardDTO {

    private int appBoardId;
    private String user;
    private int moim;
    private LocalDateTime appRegdate;
    private AppBoard.AppType appType;
    private String appTitle;
    private String appContent;
    private String appLocation;
    private LocalDateTime appStart;
    private LocalDateTime appEnd;
    private int maxAppUser;
    private int currentAppUser;

    private String userName;

    public AppBoard DTOToEntity() {
        return AppBoard.builder()
                .appBoardId(this.appBoardId)
                .user(
                        User.builder()
                                .userId(this.user)
                                .build()
                )
                .moim(Moim.builder()
                        .moimId(this.moim)
                        .build()
                )
                .appRegdate(this.appRegdate)
                .appType(this.appType)
                .appTitle(this.appTitle)
                .appLocation(this.appLocation)
                .appStart(this.appStart)
                .appEnd(this.appEnd)
                .maxAppUser(this.maxAppUser)
                .currentAppUser(this.currentAppUser)
                .build();
    }


}

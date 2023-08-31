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
    private String ownerId;
    private int moimId;
    private LocalDateTime AppRegdate;
    private AppBoard.AppType appType;
    private String appTitle;
    private String appContent;
    private String appLocation;
    private LocalDateTime AppStart;
    private LocalDateTime AppEnd;
    private int maxAppUser;
    private int currentAppUser;


}

package com.example.backend.dto;

import com.example.backend.entity.AppBoard;
import com.example.backend.entity.AppFixed;
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
public class AppFixedDTO {

    private int appFixedId;
    private int appBoardId;
    private String appFixedUser;
    private AppFixed.AppSort appSort;
    private AppFixed.AppState appState;
    private String useName;
    private int moimId;

    //앞단 보낼 땐 moimIds도 넣어서 넘기기
}

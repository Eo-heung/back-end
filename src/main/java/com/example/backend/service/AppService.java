package com.example.backend.service;

import com.example.backend.dto.AppBoardDTO;
import com.example.backend.entity.AppBoard;
import com.example.backend.entity.AppFixed;
import com.example.backend.dto.AppFixedDTO;
import com.example.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Map;

public interface AppService {
    Map<String, Object> createApp(int moimId, AppBoard appBoard, User loginUser);

    Page<AppBoard> appBoarList(int moimId, String onOff,
                               String searchType, String keyword,
                               String loginUser,
                               Pageable pageable);
    Page<AppFixedDTO> getAppMemberList(int moimId, int appBoardId, User user, Pageable pageable);
    AppBoardDTO viewAppBoard(int moimId, int appBoardId, String loginUser);

    Map<String, Object> applyToApp(int moimId, int appBoardId, String loginUser);

    void deleteApp(int moimId, int appBoardId, String loggedInUsername);



    boolean hasOverlappingAppointments(User user, LocalDateTime appStart, LocalDateTime appEnd);
}


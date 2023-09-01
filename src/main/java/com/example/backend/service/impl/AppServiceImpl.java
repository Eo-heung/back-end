package com.example.backend.service.impl;

import com.example.backend.dto.AppBoardDTO;
import com.example.backend.dto.BoardDTO;
import com.example.backend.entity.*;
import com.example.backend.repository.AppBoardRepository;
import com.example.backend.repository.AppFixedRepository;
import com.example.backend.repository.MoimRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.AppService;
import com.example.backend.service.BoardService;
import com.example.backend.service.MoimRegistrationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService {
    private final AppBoardRepository appBoardRepository;
    private final AppFixedRepository appFixedRepository;
    private final UserRepository userRepository;
    private final MoimRepository moimRepository;
    private final BoardService boardService;
    private final MoimRegistrationService moimRegistrationService;

    @Transactional
    @Override
    public AppBoard createApp(int moimId, AppBoard appBoard, User loginUser) {
        //모임 유무 확인
        Moim checkMoim = moimRepository.findById(moimId)
                .orElseThrow(() -> new NoSuchElementException("Moim not found"));

        //모임장, 모임원여부 구분
        if (boardService.verifyMemberRole(loginUser, checkMoim) || boardService.verifyLeaderRole(loginUser, checkMoim)) {
            if (hasOverlappingAppointments(loginUser, appBoard.getAppStart(), appBoard.getAppEnd())) {
                throw new IllegalStateException("이미 해당 시간에 다른 약속이 있거나 신청되어 있습니다.");
            }

            appBoard.setUser(loginUser);
            appBoard.setMoim(checkMoim);
            AppBoard savedAppBoard = appBoardRepository.save(appBoard);

            AppFixed appFixed = new AppFixed();
            appFixed.setAppBoard(savedAppBoard);
            appFixed.setAppFixedUser(loginUser);
            appFixed.setAppSort(AppFixed.AppSort.HOST);
            appFixed.setAppState(AppFixed.AppState.CONFIRM);
            appFixedRepository.save(appFixed);

            return savedAppBoard;
        } else {
            throw new IllegalStateException("이 사용자는 이 모임에 승인되지 않았습니다.");
        }

    }

    public Page<AppBoard> appBoarList(int moimId, String onOff,
                                      String searchType, String keyword,
                                      String loginUser,
                                      Pageable pageable) {
        Moim checkMoim = moimRepository.findById(moimId)
                .orElseThrow(() -> new EntityNotFoundException("모임을 찾을 수 없습니다."));

        User user = userRepository.findById(loginUser)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));

        if(!moimRegistrationService.verifyMemberRole(user, checkMoim) && !moimRegistrationService.verifyLeaderRole(user, checkMoim)) {
        } else {
            throw new IllegalStateException("이 사용자는 이 게시물을 보는 권한이 없습니다.");
        }

        AppBoard.AppType appTypeValue;
        if ("all".equals(onOff)) {
            appTypeValue = null;
        } else {
            appTypeValue = AppBoard.AppType.valueOf(onOff.toUpperCase());
        }

        return appBoardRepository.findByUserAndMoimWithConditions(loginUser, moimId, appTypeValue, searchType, keyword, pageable);
    }


    public AppBoardDTO viewAppBoard(int moimId, int appBoardId, String loginUser) {
        AppBoard appBoard = appBoardRepository.findById(appBoardId)
                .orElseThrow(() -> new RuntimeException("App Board not found"));

        Moim checkMoim = moimRepository.findById(moimId)
                .orElseThrow(() -> new RuntimeException("모임을 찾을 수 없습니다."));

        User user = userRepository.findById(loginUser)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));

        if(!moimRegistrationService.verifyMemberRole(user, checkMoim) && !moimRegistrationService.verifyLeaderRole(user, checkMoim)) {
        } else {
            throw new IllegalStateException("이 사용자는 이 게시물을 보는 권한이 없습니다.");
        }

        return appBoard.EntityToDTO(user.getUserName());
    }











    @Override
    public boolean hasOverlappingAppointments(User user, LocalDateTime appStart, LocalDateTime appEnd) {
        List<AppFixed> overlappingAppointments = appFixedRepository.findOverlappingAppointments(user, appStart, appEnd);
            return !overlappingAppointments.isEmpty();
    }


}

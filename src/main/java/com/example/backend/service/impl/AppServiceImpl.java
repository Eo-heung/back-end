package com.example.backend.service.impl;

import com.example.backend.dto.AppBoardDTO;
import com.example.backend.dto.AppFixedDTO;
import com.example.backend.dto.BoardDTO;
import com.example.backend.entity.*;
import com.example.backend.repository.*;
import com.example.backend.service.AppService;
import com.example.backend.service.BoardService;
import com.example.backend.service.CommentService;
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
    private final MoimRegistrationRepository moimRegistrationRepository;
    private final BoardService boardService;
    private final MoimRegistrationService moimRegistrationService;

    //약속 생성
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

    //약속 모집글  리스트
    public Page<AppBoard> appBoarList(int moimId, String onOff,
                                      String searchType, String keyword,
                                      String loginUser,
                                      Pageable pageable) {
        Moim checkMoim = moimRepository.findById(moimId)
                .orElseThrow(() -> new EntityNotFoundException("모임을 찾을 수 없습니다."));

        User user = userRepository.findById(loginUser)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));

        if(!moimRegistrationService.verifyMemberRole(user, checkMoim)) {
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

    //약속글 상세보기
    public AppBoardDTO viewAppBoard(int moimId, int appBoardId, String loginUser) {
        AppBoard appBoard = appBoardRepository.findById(appBoardId)
                .orElseThrow(() -> new RuntimeException("App Board not found"));

        Moim checkMoim = moimRepository.findById(moimId)
                .orElseThrow(() -> new RuntimeException("모임을 찾을 수 없습니다."));

        User user = userRepository.findById(loginUser)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));

        if(!moimRegistrationService.verifyMemberRole(user, checkMoim)) {
            throw new IllegalStateException("이 사용자는 이 게시물을 보는 권한이 없습니다.");
        }

        return appBoard.EntityToDTO(user.getUserName());
    }

    //약속 신청
    public AppFixedDTO applyToApp(int moimId, int appBoardId, String loginUser) {
        AppBoard appBoard = appBoardRepository.findById(appBoardId)
                .orElseThrow(() -> new RuntimeException("App Board not found"));

        Moim checkMoim = moimRepository.findById(moimId)
                .orElseThrow(() -> new RuntimeException("모임을 찾을 수 없습니다."));

        User user = userRepository.findById(loginUser)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));

        if(!moimRegistrationService.canAccessMoim(user, checkMoim)) {
            throw new IllegalStateException("이 사용자는 이 게시물을 보는 권한이 없습니다.");
        }

        if (appBoard.getUser().equals(user)) {
            throw new IllegalStateException("약속 게시자는 본인 약속에 참가할 수 없습니다");
        }

        if (hasAlreadyAppliedToAppointment(user, appBoard)) {
            throw new IllegalStateException("이미 해당 약속에 신청한 사용자입니다.");
        }

        LocalDateTime appStart = appBoard.getAppStart();
        LocalDateTime appEnd = appBoard.getAppEnd();

        if (hasOverlappingAppointments(user, appStart, appEnd)) {
            throw new IllegalStateException("해당 시간에 이미 다른 약속에 참여 중입니다.");
        }


        AppFixed appFixed = new AppFixed();
        appFixed.setAppBoard(appBoard);
        appFixed.setAppFixedUser(user);
        appFixed.setAppSort(AppFixed.AppSort.GUEST);
        appFixed.setAppState(AppFixed.AppState.CONFIRM);

        AppFixed savedAppFixed = appFixedRepository.save(appFixed);

        return savedAppFixed.entityToDto();
    }

    //약속 모집글 삭제
    @Transactional
    @Override
    public void deleteApp(int moimId, int appBoardId, String loggedInUsername) {
        AppBoard appBoard = appBoardRepository.findById(appBoardId)
                .orElseThrow(() -> new RuntimeException("App Board not found"));

        Moim checkMoim = moimRepository.findById(moimId)
                .orElseThrow(() -> new RuntimeException("모임을 찾을 수 없습니다."));

        User user = userRepository.findByUserId(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!moimRegistrationService.canAccessMoim(user, checkMoim)) {
            throw new IllegalStateException("이 사용자는 이 게시물을 보는 권한이 없습니다.");
        }
        Moim associatedMoim = appBoard.getMoim();

        boolean isWriter = appBoard.getUser().equals(user);
        boolean isLeader = boardService.verifyLeaderRole(user, associatedMoim);

        if (!isWriter && !isLeader) {
            throw new IllegalStateException("작성자 또는 모임장만 게시글을 삭제할 수 있습니다.");
        }

        // AppFixed 지우기
        appFixedRepository.deleteByAppBoard(appBoard);

        // AppBoard 지우기
        appBoardRepository.deleteById(appBoardId);
    }












    //동일시간대 약속 체크
    @Override
    public boolean hasOverlappingAppointments(User user, LocalDateTime appStart, LocalDateTime appEnd) {
        List<AppFixed> overlappingAppointments = appFixedRepository.findOverlappingAppointments(user, appStart, appEnd);
            return !overlappingAppointments.isEmpty();
    }

    //해당 약속에 참석 여부 체크
    private boolean hasAlreadyAppliedToAppointment(User user, AppBoard appBoard) {
        return appFixedRepository.existsByAppBoardAndAppFixedUser(appBoard, user);
    }








}

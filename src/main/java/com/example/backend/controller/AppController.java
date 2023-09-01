package com.example.backend.controller;

import com.example.backend.dto.AppBoardDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.entity.AppBoard;
import com.example.backend.entity.User;
import com.example.backend.jwt.JwtTokenProvider;
import com.example.backend.repository.AppBoardRepository;
import com.example.backend.repository.AppFixedRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.AppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appointment")
public class AppController {
    private final JwtTokenProvider jwtTokenProvider;
    private final AppService appService;
    private final UserRepository userRepository;
    private final AppBoardRepository appBoardRepository;
    private final AppFixedRepository appFixedRepository;

    //약속 만들기
    @PostMapping("/{moimId}/create-app")
    public ResponseEntity<?> createApp(@PathVariable("moimId") int moimId,
                                       @RequestBody AppBoard appBoard,
                                       @RequestHeader("Authorization") String token) {
        ResponseDTO<AppBoardDTO> responseDTO = new ResponseDTO<>();

        String loggedInUsername = jwtTokenProvider.validateAndGetUsername(token);
        User loginUser = userRepository.findByUserId(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            AppBoard createdAppBoard = appService.createApp(moimId, appBoard, loginUser);
            AppBoardDTO appBoardDTO = createdAppBoard.EntityToDTO(loginUser.getUserName());

            responseDTO.setItem(appBoardDTO);
            responseDTO.setStatusCode(HttpStatus.CREATED.value());

            return ResponseEntity.ok().body(responseDTO);


        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    //===    /appointment/{moimId}/list
    //리스트 불러오는건
    //모임제목, 온/오프라인, 날짜, 모임시작시간, 페이지네이션,
    //now 기준으로 시작시간 전이면 (모집중), 시작 시간 지났으면(진행중), 종료시간 지났으면 (약속종료+클릭금지)

    //===    /appointment/{moimId}/list/{appBoardId}
    //=== POST   /appointment/{moimId}/list/{appBoardId}/apply
    //상세 약속 겸 신청
    //사진은 신청할 떄는 필요 없고, 조회할 때

    //===    /appointment/{moimId}/list/{appBoardId}/delete
    //삭제  > appfixed 랑 appboard

    //===    /appointment/{moimId}/list/{appBoardId}/member-list
    //신청자 확인 리스트 >  닉네임, 프로필사진(모임사진 끌어올거니까), 지역
    //(상세는 X)


    //내 약속은 봐서 ㅇㅇ
    //사진첩 뺴자

    //모임장이 모임 내보내기 => 모임가입한 사람들 조회 리스트 필요











}

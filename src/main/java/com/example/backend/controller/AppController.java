package com.example.backend.controller;

import com.example.backend.dto.AppBoardDTO;
import com.example.backend.dto.AppFixedDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.entity.AppBoard;
import com.example.backend.entity.AppFixed;
import com.example.backend.entity.User;
import com.example.backend.jwt.JwtTokenProvider;
import com.example.backend.repository.AppBoardRepository;
import com.example.backend.repository.AppFixedRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.AppService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appointment")
public class AppController {
    private final JwtTokenProvider jwtTokenProvider;
    private final AppService appService;
    private final UserRepository userRepository;
    private final AppBoardRepository appBoardRepository;
    private final AppFixedRepository appFixedRepository;

    //약속 생성
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

    //약속 모집글 리스트
    //모임제목, 온/오프라인, 날짜, 모임시작시간, 페이지네이션,
    //[앞단] now 기준으로 시작시간 전이면 (모집중), 시작 시간 지났으면(진행중), 종료시간 지났으면 (약속종료+클릭금지)
    @GetMapping("/{moimId}/list")
    public ResponseEntity<?> getAppList(
            @PathVariable("moimId") int moimId,
            @RequestParam(required = false, defaultValue = "all") String onOff,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "all") String searchType,
            @RequestParam(defaultValue = "descending") String orderBy,
            @RequestParam(defaultValue = "0") int currentPage,
            @RequestHeader("Authorization") String token,
            @PageableDefault(size = 10) Pageable pageable) {

        String loggedInUsername = jwtTokenProvider.validateAndGetUsername(token);
        User loginUser = userRepository.findByUserId(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (orderBy.equals("ascending")) {
            pageable = PageRequest.of(currentPage, 10, Sort.by("appRegdate").ascending());
        } else {
            pageable = PageRequest.of(currentPage, 10, Sort.by("appRegdate").descending());
        }

        ResponseDTO<Page<AppBoardDTO>> response = new ResponseDTO<>();

        try {
            Page<AppBoard> appPage = appService.appBoarList(moimId, onOff, searchType, keyword, loggedInUsername, pageable);

            Page<AppBoardDTO> appBoardDTOPage = appPage.map(app -> app.EntityToDTO(loginUser.getUserName()));

            ResponseDTO.PaginationInfo paginationInfo = new ResponseDTO.PaginationInfo();
            paginationInfo.setTotalPages(appBoardDTOPage.getTotalPages());
            paginationInfo.setCurrentPage(appBoardDTOPage.getNumber());
            paginationInfo.setTotalElements(appBoardDTOPage.getTotalElements());

            response.setItem(appBoardDTOPage);
            response.setLastPage(appBoardDTOPage.isLast());
            response.setPaginationInfo(paginationInfo);
            response.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(response);
        } catch (NoSuchElementException | IllegalStateException e) {
            response.setStatusCode(HttpStatus.FORBIDDEN.value());
            response.setErrorMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }

    //약속글 상세보기
    @GetMapping("/{moimId}/list/{appBoardId}")
    public ResponseEntity<?> getAppMemberList(
            @PathVariable int moimId,
            @PathVariable int appBoardId,
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page) {

        String loginUser = jwtTokenProvider.validateAndGetUsername(token);
        User checkUser = userRepository.findByUserId(loginUser)
                .orElseThrow(() -> new RuntimeException("User not found"));


        Pageable pageable = PageRequest.of(0, (page + 1) * 3);
        Page<AppFixedDTO> appMemberListPage = appService.getAppMemberList(moimId, appBoardId,checkUser ,pageable);
        System.out.println(appMemberListPage);

        ResponseDTO<AppFixedDTO> responseDTO = new ResponseDTO<>();
        try {
            List<AppFixedDTO> result = appMemberListPage.getContent();

            responseDTO.setItems(result);
            responseDTO.setLastPage(appMemberListPage.isLast());
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);

        } catch(Exception e) {
            System.out.println(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }


    //약속 신청
    @PostMapping("/{moimId}/list/{appBoardId}/apply")
    public ResponseEntity<?> applyToApp(@PathVariable int moimId,
                                           @PathVariable int appBoardId,
                                           @RequestHeader("Authorization") String token) {
        ResponseDTO<AppFixedDTO> responseDTO = new ResponseDTO<>();
        String loggedInUsername = jwtTokenProvider.validateAndGetUsername(token);

        User loginUser = userRepository.findByUserId(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            AppFixedDTO appFixedDTO = appService.applyToApp(moimId, appBoardId, loginUser.getUserId());

            responseDTO.setItem(appFixedDTO);
            responseDTO.setStatusCode(HttpStatus.CREATED.value());

            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //약속 모집글 삭제
    @DeleteMapping("/{moimId}/list/{appBoardId}/delete")
    public ResponseEntity<?> deleteApp(@PathVariable("moimId") int moimId,
                                       @PathVariable("appBoardId") int appBoardId,
                                       @RequestHeader("Authorization") String token) {
        ResponseDTO<String> responseDTO = new ResponseDTO<>();
        String loggedInUsername = jwtTokenProvider.validateAndGetUsername(token);
        System.out.println(loggedInUsername);
        try {
            appService.deleteApp(moimId, appBoardId, loggedInUsername);
            responseDTO.setItem("약속 모집글을 성공적으로 삭제했습니다.");
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //약속 모집글 삭제
    //신청자 확인 리스트 >  닉네임, 프로필사진(모임사진 끌어올거니까), 지역
    //(상세는 X)


    @GetMapping("/{moimId}/list/{appBoardId}/member-list")
    public ResponseEntity<Page<AppFixedDTO>> getAppMemberList(@PathVariable(required = false,value ="moimId") int moimId,
                                                              @PathVariable(required = false,value = "appBoardId") int appBoardId,
                                                              Pageable pageable,
                                                              @RequestHeader("Authorization") String token) {
        System.out.println(moimId+"appBoardId"+appBoardId+"token"+token);
        String loggedInUsername = jwtTokenProvider.validateAndGetUsername(token);
        System.out.println(loggedInUsername);
        User loginUser = userRepository.findByUserId(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println(loginUser);
        return ResponseEntity.ok(appService.getAppMemberList(moimId, appBoardId, loginUser, pageable));
    }


    //===    /appointment/{moimId}/list/{appBoardId}
    //=== POST   /appointment/{moimId}/list/{appBoardId}/apply
    //상세 약속 겸 신청
    //사진은 신청할 떄는 필요 없고, 조회할 때



    //내 약속은 봐서 ㅇㅇ
    //사진첩 뺴자

    //모임장이 모임 내보내기 => 모임가입한 사람들 조회 리스트 필요











}

package com.example.backend.controller;

import com.example.backend.dto.MoimDTO;
import com.example.backend.dto.MoimPictureDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.entity.*;
import com.example.backend.jwt.JwtTokenProvider;
import com.example.backend.repository.*;
import com.example.backend.service.MoimPictureService;
import com.example.backend.service.MoimService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moim")
public class MoimController {

    private final MoimService moimService;
    private final MoimPictureService moimPictureService;
    private final MoimRepository moimRepository;
    private final MoimPictureRepository moimPictureRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final MoimRegistrationRepository moimRegistrationRepository;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final BoardPictureRepository boardPictureRepository;
    private final AppBoardRepository appBoardRepository;
    private final AppFixedRepository appFixedRepository;

    @PostMapping("/create-moim")
    public ResponseEntity<?> createMoim(@RequestBody MoimDTO moimDTO,
                                        @RequestHeader("Authorization") String token) {
        ResponseDTO<MoimDTO> responseDTO = new ResponseDTO<>();

        String loginUser = jwtTokenProvider.validateAndGetUsername(token);
        User checkUser = userRepository.findByUserId(loginUser)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            moimDTO.setMoimRegdate(LocalDateTime.now());
            Moim moim = moimService.createMoim(moimDTO.DTOToEntity(), checkUser);



            responseDTO.setItem(moim.EntityToDTO());
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);

        } catch(Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/create-moim-pic")
    public ResponseEntity<?> createMoimPic(@RequestParam("moimPic") MultipartFile file,
                                           @RequestParam("moimId") Integer moimId) {
        ResponseDTO<MoimPictureDTO> responseDTO = new ResponseDTO<>();

        try {
            System.out.println(moimId);

            // moimId를 통해서 모임의 데이터를 다 가지고 옴.
            Moim moim = moimService.viewMoim(moimId);

            MoimPicture moimPicture = MoimPicture.builder()
                    .userId(moim.getUserId())
                    .moimId(
                            Moim.builder()
                                    .moimId(moim.getMoimId())
                                    .build()
                    )
                    .moimPic(file.getBytes())
                    .createPic(LocalDateTime.now())
                    .updatePic(LocalDateTime.now())
                    .build();

            MoimPicture updatePic = moimPictureService.createMoim(moimPicture);

            MoimPictureDTO moimPictureDTO = updatePic.EntityToDTO();

            responseDTO.setItem(moimPictureDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);

        } catch(Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/list-moim/asc")
    public ResponseEntity<?> getMoimListAsc(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(required = false, defaultValue = "전체") String category,
                                            @RequestParam(required = false) String searchKeyword,
                                            @RequestParam(required = false, defaultValue = "all") String searchType,
                                            @RequestParam(defaultValue = "descending") String orderBy,
                                            @RequestHeader("Authorization") String token) {

        return getResponse(page, category, searchKeyword, searchType, "ascending", token);
    }

    @PostMapping("/list-moim/desc")
    public ResponseEntity<?> getMoimListDesc(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(required = false, defaultValue = "전체") String category,
                                             @RequestParam(required = false, defaultValue = "") String searchKeyword,
                                             @RequestParam(required = false, defaultValue = "all") String searchType,
                                             @RequestParam(defaultValue = "descending") String orderBy,
                                             @RequestHeader("Authorization") String token) {

        return getResponse(page, category, searchKeyword, searchType, "descending", token);
    }

    @PostMapping(value ="/my-moim-list")
    public ResponseEntity<?> getMyMoim(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(defaultValue = "descending") String orderBy,
            @RequestParam(defaultValue = "0") int page) {

        String loginUser = jwtTokenProvider.validateAndGetUsername(token);
        User checkUser = userRepository.findByUserId(loginUser)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pageable pageable = PageRequest.of(0, (page + 1) * 3);

        Page<Moim> mymoimPage = moimService.getMyMoim(loginUser, keyword, orderBy, pageable);

        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();
        try {
            List<Map<String, Object>> result = new ArrayList<>();

            for (Moim moim : mymoimPage.getContent()) {
                int moimId = moim.getMoimId();
                Map<String, Object> returnMap = new HashMap<>();
                MoimPicture matchingPicture = moim.getMoimPicture();

                if (matchingPicture != null) {
                    String base64Encoded = Base64.getEncoder().encodeToString(matchingPicture.getMoimPic());
                    returnMap.put("moimPic", base64Encoded);
                }

                returnMap.put("moimId", moim.getMoimId());
                returnMap.put("moimCategory", moim.getMoimCategory());
                returnMap.put("moimTitle", moim.getMoimTitle());
                returnMap.put("moimAddr", moim.getMoimAddr());
                returnMap.put("maxMoimUser", moim.getMaxMoimUser());
                returnMap.put("currentMoimUser", moim.getCurrentMoimUser());
                returnMap.put("moimContent", moim.getMoimContent());
                result.add(returnMap);
            }

            responseDTO.setItems(result);
            responseDTO.setLastPage(mymoimPage.isLast());
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);

        } catch(Exception e) {
            System.out.println(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    @GetMapping("/view-moim/{moimId}")
    public ResponseEntity<?> viewMoim(@PathVariable int moimId) {
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();

        try {
            Moim moim = moimService.viewMoim(moimId);

            MoimPicture moimPicture = moimPictureService.getPic(
                    Moim.builder()
                            .moimId(moimId)
                            .build()
            );

            // MoimDTO 얻기
            MoimDTO returnMoimDTO = moim.EntityToDTO();
            // MoimPic 얻기
            String base64Encoded = Base64.getEncoder().encodeToString(moimPicture.getMoimPic());

            Map<String, Object> returnMap = new HashMap<>();

            // 각각 returnMap에 담아주기
            returnMap.put("moimDTO", returnMoimDTO);
            returnMap.put("moimPic", base64Encoded);

            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            System.out.println("+++++++++++++++++++++++++++++++++++++++");
            System.out.println(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }



    @PostMapping(value = "/modify-moim/{moimId}")
    public ResponseEntity<?> modifyMoim(@PathVariable int moimId, @RequestBody MoimDTO moimDTO) {
        ResponseDTO<Moim> responseDTO = new ResponseDTO<>();


        try {
            Moim moim = moimService.viewMoim(moimId);

            moim.setMoimTitle(moimDTO.getMoimTitle());
            moim.setMoimCategory(moimDTO.getMoimCategory());
            moim.setMoimContent(moimDTO.getMoimContent());
            moim.setMoimAddr(moimDTO.getMoimAddr());
            moim.setMaxMoimUser(moimDTO.getMaxMoimUser());
            moim.setCurrentMoimUser(moimDTO.getCurrentMoimUser());

            Moim editMoim = moimService.modifyMoim(moim);

            responseDTO.setItem(editMoim);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping(value = "/modify-moim-pic/{moimId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> modifyMoim(@PathVariable int moimId,
                                        @RequestParam(required = false, value = "moimPic") MultipartFile moimPic) {
        ResponseDTO<MoimPicture> responseDTO = new ResponseDTO<>();
        System.out.println(moimId);
        System.out.println(moimPic);
        try {

            MoimPicture moimPicture = moimPictureService.getPic(
                    Moim.builder()
                            .moimId(moimId)
                            .build()
            );

            moimPicture.setMoimPic(moimPic.getBytes());
            moimPicture.setUpdatePic(LocalDateTime.now());

            MoimPicture moimPicture1 = moimPictureService.createMoim(moimPicture);

            responseDTO.setItem(moimPicture1);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @DeleteMapping("/delete-moim/{moimId}")
    public ResponseEntity<?> deleteMoim(@PathVariable int moimId) {
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();

        try {
            Moim moim = moimRepository.findById(moimId)
                    .orElseThrow(() -> new Exception("게시글을 찾을 수 없습니다"));

            //댓글 삭제
            List<Comment> comments = commentRepository.findByBoardIdMoimId(moim);
            commentRepository.deleteAll(comments);

            // 게시글 사진 삭제
            List<BoardPicture> boardPictures = boardPictureRepository.findByMoimId(moim);
            boardPictureRepository.deleteAll(boardPictures);

            //게시글 삭제
            List<Board> boards = boardRepository.findByMoimId(moim);
            boardRepository.deleteAll(boards);

            //모임 약속 상태 삭제
            List<AppFixed> appFixeds = appFixedRepository.findByAppBoard_Moim(moim);
            appFixedRepository.deleteAll(appFixeds);

            //모임 약속 게시글 삭제
            List<AppBoard> appBoards = appBoardRepository.findByMoim(moim);
            appBoardRepository.deleteAll(appBoards);

            //가입 신청 삭제
            List<MoimRegistration> registrations = moimRegistrationRepository.findAllByMoim(moim);
            moimRegistrationRepository.deleteAll(registrations);

            //모임 사진 삭제
            MoimPicture moimPicture = moimPictureRepository.findByMoim(moim);
            moimPictureRepository.delete(moimPicture);

            //모임 삭제
            moimRepository.delete(moim);



            Map<String, String> returnMap = new HashMap<>();
            returnMap.put("msg", "정상적으로 삭제되었습니다.");
            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    private ResponseEntity<?> getResponse(int page, String category, String searchKeyword, String searchType, String orderBy, String token) {
        String userId = jwtTokenProvider.validateAndGetUsername(token);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Pageable pageable = PageRequest.of(0, (page + 1) * 3);

        Page<Moim> moimPage;

        if ("ascending".equals(orderBy)) {
            moimPage = moimService.searchMoims(user, category, searchKeyword, searchType, orderBy, pageable);
        } else {
            moimPage = moimService.searchMoims(user, category, searchKeyword, searchType, orderBy, pageable);
        }

        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();

        try {
            List<Map<String, Object>> result = new ArrayList<>();

            for (Moim moim : moimPage.getContent()) {
                int moimId = moim.getMoimId();
                Map<String, Object> returnMap = new HashMap<>();
                MoimPicture matchingPicture = moim.getMoimPicture();

                if (matchingPicture != null) {
                    String base64Encoded = Base64.getEncoder().encodeToString(matchingPicture.getMoimPic());
                    returnMap.put("moimPic", base64Encoded);
                }

                returnMap.put("moimId", moim.getMoimId());
                returnMap.put("moimCategory", moim.getMoimCategory());
                returnMap.put("moimTitle", moim.getMoimTitle());
                returnMap.put("moimAddr", moim.getMoimAddr());
                returnMap.put("maxMoimUser", moim.getMaxMoimUser());
                returnMap.put("currentMoimUser", moim.getCurrentMoimUser());
                returnMap.put("moimContent", moim.getMoimContent());
                String name = "moim" + moimId;
                result.add(returnMap);
            }
            responseDTO.setItems(result);
            responseDTO.setLastPage(moimPage.isLast());
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);

        }
    }







}

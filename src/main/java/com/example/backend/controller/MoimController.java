package com.example.backend.controller;

import com.example.backend.dto.MoimDTO;
import com.example.backend.dto.MoimPictureDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.entity.Moim;
import com.example.backend.entity.MoimPicture;
import com.example.backend.entity.User;
import com.example.backend.jwt.JwtTokenProvider;
import com.example.backend.repository.MoimPictureRepository;
import com.example.backend.repository.MoimRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.MoimPictureService;
import com.example.backend.service.MoimService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moim")
//@Transactional
public class MoimController {

    private final MoimService moimService;
    private final MoimPictureService moimPictureService;
    private final MoimRepository moimRepository;
    private final MoimPictureRepository moimPictureRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @PostMapping("/create-moim")
    public ResponseEntity<?> createMoim(@RequestBody MoimDTO moimDTO) {
        ResponseDTO<MoimDTO> responseDTO = new ResponseDTO<>();

        try {
            moimDTO.setMoimRegdate(LocalDateTime.now());

            Moim moim = moimService.createMoim(moimDTO.DTOToEntity());

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
                                            @RequestParam(defaultValue = "ascending") String orderBy,
                                            @RequestHeader("Authorization") String token) {
        System.out.println("카테고리1컨");
        System.out.println(category);
        System.out.println(searchType);
        System.out.println(searchKeyword);

        return getResponse(page, category, searchKeyword, searchType, "ascending", token);
    }

    @PostMapping("/list-moim/desc")
    public ResponseEntity<?> getMoimListDesc(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(required = false, defaultValue = "전체") String category,
                                             @RequestParam(required = false, defaultValue = "") String searchKeyword,
                                             @RequestParam(required = false, defaultValue = "all") String searchType,
                                             @RequestParam(defaultValue = "ascending") String orderBy,
                                             @RequestHeader("Authorization") String token) {

        System.out.println("카테고리1컨");
        System.out.println(category);
        System.out.println(searchType);
        System.out.println(searchKeyword);

        return getResponse(page, category, searchKeyword, searchType, "descending", token);
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
//            카테고리, 소모임 이름, 모임 주소, 현재 가입한 회원 수, 최대 인원, 모임 소개, 그림
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

            MoimPicture moimPicture = moimPictureRepository.findByMoim(moim);

            moimPictureRepository.delete(moimPicture);

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

        System.out.println("카테고리2컨");
        System.out.println(category);
        System.out.println(searchType);
        System.out.println(searchKeyword);

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

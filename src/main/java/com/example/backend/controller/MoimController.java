package com.example.backend.controller;

import com.example.backend.dto.MoimDTO;
import com.example.backend.dto.MoimPictureDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.entity.Moim;
import com.example.backend.entity.MoimPicture;
import com.example.backend.entity.User;
import com.example.backend.jwt.CustomUserDetails;
import com.example.backend.jwt.JwtTokenProvider;
import com.example.backend.repository.MoimPictureRepository;
import com.example.backend.repository.MoimRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.MoimPictureService;
import com.example.backend.service.MoimService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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
    private final UserRepository userRepository;
    private final MoimRepository moimRepository;
    private final MoimPictureRepository moimPictureRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/create-moim")
    public ResponseEntity<?> createMoim(@RequestBody MoimDTO moimDTO) {
        ResponseDTO<MoimDTO> responseDTO = new ResponseDTO<>();

        try {
            moimDTO.setMoimRegdate(LocalDateTime.now());

            Moim moim = moimService.createMoim(moimDTO.DTOToEntity());

            responseDTO.setItem(moim.EntityToDTO());

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

            return ResponseEntity.ok().body(responseDTO);

        } catch(Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }


    @GetMapping("/list-moim")
    public ResponseEntity<?> getMoimList() {
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();

        try {
            //모임 리스트 불러오기
            List<Moim> moimList = moimService.getMoimList();
            List<MoimPicture> moimPictureList = moimPictureService.getPictureList();

            Map<String, Object> result = new HashMap<>();


//            카테고리, 소모임 이름, 모임 주소, 현재 가입한 회원 수, 최대 인원, 모임 소개, 그림

            int i = 0;

            for(Moim moim : moimList) {

                int moimId = moim.getMoimId();
                Map<String, Object> returnMap = new HashMap<>();

                if(moimPictureList.get(i).getMoimId().getMoimId() == moimId ) {

                    String base64Encoded = Base64.getEncoder().encodeToString(moimPictureList.get(i).getMoimPic());
                    returnMap.put("moimId", moim.getMoimId());
                    returnMap.put("moimPic", base64Encoded);
                    returnMap.put("moimCategory", moim.getMoimCategory());
                    returnMap.put("moimTitle", moim.getMoimTitle());
                    returnMap.put("moimAddr", moim.getMoimAddr());
//                    returnMap.put("currentUser", moim.getCurrentUser);
                    returnMap.put("maxMoimUser", moim.getMaxMoimUser());
                    returnMap.put("moimContent", moim.getMoimContent());
                    if(moimPictureList.size() - 1 > i ) {
                        i++;
                    }

                } else {
                    returnMap.put("moimId", moim.getMoimId());
                    returnMap.put("moimCategory", moim.getMoimCategory());
                    returnMap.put("moimTitle", moim.getMoimTitle());
                    returnMap.put("moimAddr", moim.getMoimAddr());
//                    returnMap.put("currentUser", moim.getCurrentUser);
                    returnMap.put("maxMoimUser", moim.getMaxMoimUser());
                    returnMap.put("moimContent", moim.getMoimContent());
                }

                System.out.println(returnMap);

                String name = "moim" + moimId;
                result.put(name, returnMap);
            }

            responseDTO.setItem(result);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);

        } catch(Exception e) {
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
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }


//    @PutMapping(value = "/modify-moim-pic/{moimId}")
//    public ResponseEntity<?> modifyMoim(@PathVariable int moimId,
//                                        MoimPictureDTO moimPictureDTO,
//                                        @RequestParam(required = false, value = "moimPic") MultipartFile moimPic)
//            throws Exception {
//        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();
//        try {
//
////            User user = userRepository.findByUserId(moimDTO.getUserId()).get();
//
//            moimService.modifyMoim(writeMoim);
//
//            Moim modifyMoim = moimService.viewMoim(moimId);
//            MoimDTO returnMoimDTO = modifyMoim.EntityToDTO();
//
//
//            byte[] picBytes = moimPic.getBytes();
//
//            MoimPicture modifyPic = moimPictureRepository.findById(moimId).get();
//            modifyPic.setMoimPic(picBytes);
//            moimPictureService.modifyPic(modifyPic);
//
//            MoimPicture returnMoimPicDTO = moi
//
//
//            moimPictureService.modifyPic(writePicture);
//
//
//
//
//            Map<String, Object> returnMap = new HashMap<>();
//            returnMap.put("moim", returnMoimDTO);
//
//            returnMap.put("msg", "수정 완료되었습니다.");
//
//            responseDTO.setItem(returnMap);
//
//            return ResponseEntity.ok().body(responseDTO);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
//            responseDTO.setErrorMessage(e.getMessage());
//            return ResponseEntity.badRequest().body(responseDTO);
//        }
//    }
//
//
//    @PutMapping(value = "/modify-moim/{moimId}")
//    public ResponseEntity<?> modifyMoim(@PathVariable int moimId,
//                                        MoimDTO moimDTO) {
//        User user = moimRe.getMyInfo(userDetails.getUsername());
//
//        try {
//
////            User user = userRepository.findByUserId(moimDTO.getUserId()).get();
//
//            moimService.modifyMoim(writeMoim);
//
//            Moim modifyMoim = moimService.viewMoim(moimId);
//            MoimDTO returnMoimDTO = modifyMoim.EntityToDTO();
//
//
//            byte[] picBytes = moimPic.getBytes();
//
//            MoimPicture modifyPic = moimPictureRepository.findById(moimId).get();
//            modifyPic.setMoimPic(picBytes);
//            moimPictureService.modifyPic(modifyPic);
//
//            MoimPicture returnMoimPicDTO = moi
//
//
//            moimPictureService.modifyPic(writePicture);
//
//
//
//
//            Map<String, Object> returnMap = new HashMap<>();
//            returnMap.put("moim", returnMoimDTO);
//
//            returnMap.put("msg", "수정 완료되었습니다.");
//
//            responseDTO.setItem(returnMap);
//
//            return ResponseEntity.ok().body(responseDTO);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
//            responseDTO.setErrorMessage(e.getMessage());
//            return ResponseEntity.badRequest().body(responseDTO);
//        }
//    }


    @DeleteMapping("/delete-moim")
    public ResponseEntity<?> deleteMoim(@RequestParam("moimId") int moimId) {
        ResponseDTO<Map<String, String>> responseDTO =
                new ResponseDTO<Map<String, String>>();

        try {
            Moim moim = moimRepository.findById(moimId)
                    .orElseThrow(() -> new Exception("게시글을 찾을 수 없습니다"));

            MoimPicture moimPicture = moimPictureRepository.findByMoim(moim);

            moimPictureRepository.delete(moimPicture);

            moimRepository.delete(moim);

            Map<String, String> returnMap = new HashMap<String, String>();
            returnMap.put("msg", "정상적으로 삭제되었습니다.");
            responseDTO.setItem(returnMap);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }




}

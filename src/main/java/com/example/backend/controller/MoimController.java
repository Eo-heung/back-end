package com.example.backend.controller;

import com.example.backend.dto.MoimDTO;
import com.example.backend.dto.MoimPictureDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.entity.Moim;
import com.example.backend.entity.MoimPicture;
import com.example.backend.repository.MoimPictureRepository;
import com.example.backend.repository.MoimRepository;
import com.example.backend.service.MoimPictureService;
import com.example.backend.service.MoimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

                if(moimPictureList.get(i).getMoimId().getMoimId() == moimId) {

                    String base64Encoded = Base64.getEncoder().encodeToString(moimPictureList.get(i).getMoimPic());
                    returnMap.put("moimId", moim.getMoimId());
                    returnMap.put("moimPic", base64Encoded);
                    returnMap.put("moimCategory", moim.getMoimCategory());
                    returnMap.put("moimTitle", moim.getMoimTitle());
                    returnMap.put("moimAddr", moim.getMoimAddr());
                    returnMap.put("maxMoimUser", moim.getMaxMoimUser());
                    returnMap.put("currentMoimUser", moim.getCurrentMoimUser());
                    returnMap.put("moimContent", moim.getMoimContent());
                    if(moimPictureList.size() - 1 > i ) {
                        i++;
                    }

                } else {
                    returnMap.put("moimId", moim.getMoimId());
                    returnMap.put("moimCategory", moim.getMoimCategory());
                    returnMap.put("moimTitle", moim.getMoimTitle());
                    returnMap.put("moimAddr", moim.getMoimAddr());
                    returnMap.put("maxMoimUser", moim.getMaxMoimUser());
                    returnMap.put("currentMoimUser", moim.getCurrentMoimUser());
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




}

package com.example.backend.controller;

import com.example.backend.dto.MoimDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.entity.Moim;
import com.example.backend.entity.User;
import com.example.backend.jwt.CustomUserDetails;
import com.example.backend.repository.MoimRepository;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moim")
public class MoimController {

    private final MoimService moimService;

    @Value("${file.path}")
    String savePicPath;

//    @PostMapping("/create-moim", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping(value = "/create-moim", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createMoim(MoimDTO moimDTO,
                                        @RequestParam(required = false, value = "file") MultipartFile moimPic) {
        ResponseDTO<Map<String, Object> > responseDTO = new ResponseDTO<>();
        try {
            LocalDateTime moimRegdate = LocalDateTime.now();
            Moim moim = moimDTO.DTOToEntity(moimRegdate);


            moimService.createMoim(moim, moimPic);

            System.out.println("이양" + moim);

            Map<String, Object> returnMap = new HashMap<>();

            responseDTO.setItem(returnMap);
            return ResponseEntity.ok().body(responseDTO);

        } catch(Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/list-moim")
    public ResponseEntity<?> getMoimList(@PageableDefault(page = 0, size = 4) Pageable pageable, String moimnickname,
                                         @RequestParam(value = "searchCondition", required = false) String searchCondition,
                                         @RequestParam(value = "searchKeyword", required = false) String searchKeyword) {
        ResponseDTO<MoimDTO> responseDTO = new ResponseDTO<>();

        try {
            searchCondition = searchCondition == null ? "all" : searchCondition;
            searchKeyword = searchKeyword == null ? "" : searchKeyword;

            Page<Moim> pageMoim = moimService.listMoim(pageable, searchCondition, moimnickname, searchKeyword);

            Page<MoimDTO> pageMoimDTO = pageMoim.map(Moim::EntityToDTO);


            responseDTO.setPageItems(pageMoimDTO);
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

            MoimDTO returnMoimDTO = moim.EntityToDTO();

            Map<String, Object> returnMap = new HashMap<>();

            returnMap.put("moim", returnMoimDTO);

            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }


    @PutMapping(value = "/moim")
    public ResponseEntity<?> modifyBoard(@RequestBody MoimDTO moimDTO)
            throws Exception {
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();
        try {
            Moim writeMoim = moimService.viewMoim(moimDTO.getMoimId());
            LocalDateTime originalRegdate = writeMoim.getMoimRegdate();

            Moim moim = moimDTO.DTOToEntity(originalRegdate);

            moimService.modifyMoim(moim);

            Map<String, Object> returnMap = new HashMap<>();

            Moim modifyMoim = moimService.viewMoim(moim.getMoimId());

            MoimDTO returnMoimDTO = modifyMoim.EntityToDTO();

            returnMap.put("moim", returnMoimDTO);
            returnMap.put("msg", "수정 완료되었습니다.");

            responseDTO.setItem(returnMap);

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }


    @DeleteMapping("/view-moim/{moimId}")
    public ResponseEntity<?> deleteMoim(@PathVariable int moimId) {
        ResponseDTO<Map<String, String>> responseDTO =
                new ResponseDTO<Map<String, String>>();

        try {
            moimService.deleteMoim(moimId);

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

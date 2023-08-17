package com.example.backend.controller;

import com.example.backend.dto.MoimDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.entity.Moim;
import com.example.backend.entity.User;
import com.example.backend.jwt.CustomUserDetails;
import com.example.backend.repository.MoimRepository;
import com.example.backend.service.MoimService;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moim")
public class MoimController {

    private final MoimRepository moimRepository;
    private final MoimService moimService;




    @PostMapping("/create-moim")
    public ResponseEntity<?> createMoim(@RequestBody MoimDTO moimDTO) {
        ResponseDTO<Map<String, Object> > responseDTO = new ResponseDTO<>();
        System.out.println("1번 확인");
        try {
            System.out.println("2번 확인");
            Moim moim = moimDTO.DTOToEntity();
            System.out.println(moim);
            moimService.createMoim(moim);

            Map<String, Object> returnMap = new HashMap<>();

            returnMap.put("msg", "모임 등록이 완료되었습니다.");
            responseDTO.setItem(returnMap);

            System.out.println("3번 확인");

            return ResponseEntity.ok().body(responseDTO);


        } catch(Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            System.out.println("4번 확인");


            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

//    @Value("${file.path}")
//    String attachPath;
//
//    @PostMapping(value = "/create-moim", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?> createMoim(MoimDTO moimDTO, MultipartHttpServletRequest mphsRequest) {
//        ResponseDTO<Map<String, Object> > responseDTO = new ResponseDTO<>();
//
//        File directory = new File(attachPath);
//
//        if(!directory.exists()) {
//            directory.mkdir();
//        }
//
//        try {
//            Moim moim = moimDTO.DTOToEntity();
//
//            moimService.createMoim(moim);
//
//            Map<String, Object> returnMap =
//                    new HashMap<>();
//
//            returnMap.put("msg", "모임 등록이 완료되었습니다.");
//            responseDTO.setItem(returnMap);
//            return ResponseEntity.ok().body(responseDTO);
//
//        } catch(Exception e) {
//            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
//            responseDTO.setErrorMessage(e.getMessage());
//
//            return ResponseEntity.badRequest().body(responseDTO);
//        }
//    }


//
//    @GetMapping("/list-moim")
//    public ResponseEntity<?> getBoardList(@PageableDefault(page = 0, size = 10) Pageable pageable,
//                                          @AuthenticationPrincipal CustomUserDetails customUserDetails, User user,
//                                          @RequestParam(value = "searchCondition", required = false) String searchCondition,
//                                          @RequestParam(value = "searchKeyword", required = false) String searchKeyword) {
//        ResponseDTO<MoimDTO> responseDTO = new ResponseDTO<>();
//
//        try {
//            searchCondition = searchCondition == null ? "all" : searchCondition;
//            searchKeyword = searchKeyword == null ? "" : searchKeyword;
//
//            Page<Moim> pageMoim = moimService.listMoim(pageable, searchCondition, user, searchKeyword);
//
//            Page<MoimDTO> pageMoimDTO = pageMoim.map(Moim::EntityToDTO);
//
//
//            responseDTO.setPageItems(pageMoimDTO);
//            responseDTO.setStatusCode(HttpStatus.OK.value());
//
//            return ResponseEntity.ok().body(responseDTO);
//
//        } catch(Exception e) {
//            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
//            responseDTO.setErrorMessage(e.getMessage());
//
//            return ResponseEntity.badRequest().body(responseDTO);
//        }
//    }
//
//








}

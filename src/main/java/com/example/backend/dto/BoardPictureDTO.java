package com.example.backend.dto;

import com.example.backend.entity.Moim;
import com.example.backend.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardPictureDTO {

    private int boardPicId; //게시글 사진 Id
    private User userId; //모임장, 모임원 확인
    private Moim moimId; //모임Id
    private byte[] boardPic; //사진저장
    private LocalDateTime createboardPic; //등록일
    private LocalDateTime updateboardPic; //수정일



}

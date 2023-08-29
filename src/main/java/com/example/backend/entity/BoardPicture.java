package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "BOARD_PICTURE")
public class BoardPicture {

    @Id
    @Column(name = "board_pic_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int boardPicId; //게시글 사진 Id

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId; //모임장, 모임원 확인

    @ManyToOne
    @JoinColumn(name = "moim_id")
    private Moim moimId; //모임Id

    @Lob
    @Column(name = "board_pic", columnDefinition = "MEDIUMBLOB")
    private byte[] boardPic; //사진저장


    @Column(name = "create_board_pic", updatable = false)
    private LocalDateTime createBoardPic; //등록일

    @Column(name = "update_board_pic")
    private LocalDateTime updateBoardPic; //수정일

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

}

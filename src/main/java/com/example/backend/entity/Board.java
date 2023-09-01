package com.example.backend.entity;

import com.example.backend.dto.BoardDTO;
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
@Table(name = "BOARD")
public class Board {

    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int boardId; //게시글Id

    @Enumerated(EnumType.STRING)
    @Column(name = "board_type")
    private BoardType boardType; //공지, 자유

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne
    @JoinColumn(name = "moim_id")
    private Moim moimId; //모임Id

    @Column(name = "board_title")
    private String boardTitle;

    @Column(name = "board_content")
    private String boardContent;

    @Column(name = "board_regdate")
    private LocalDateTime boardRegdate;

    @Column(name = "boarde_update")
    private LocalDateTime boardUpdate;

    @Column(name="is_Public")
    private boolean isPublic;   //공개여부



    public enum BoardType {
        FREE, //자유
        NOTICE  // 공지
    }



    public BoardDTO EntityToDTO() {
        return BoardDTO.builder()
                .boardId(this.boardId)
                .boardType(this.boardType)
                .userId(this.userId.getUserId())
                .moimId(this.moimId.getMoimId())
                .boardTitle(this.boardTitle)
                .boardContent(this.boardContent)
                .boardRegdate(this.boardRegdate)
                .boardUpdate(this.boardUpdate)
                .isPublic(this.isPublic)
                .build();
    }




}

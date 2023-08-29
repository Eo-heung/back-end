package com.example.backend.dto;

import com.example.backend.entity.Board;
import com.example.backend.entity.Moim;
import com.example.backend.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDTO {

    private int boardId; //게시글Id
    private Board.BoardType boardType;
    private String userId; //모임장, 모임원 확인
    private int moimId; //모임Id
    private String userName;
    private String boardTitle;
    private String boardContent;
    private LocalDateTime boardRegdate;
    private LocalDateTime boardUpdate;
    private boolean isPublic;   //공개여부
    private boolean isLeader; //모임장 여부
    private boolean isMember; //모임원 여부

    public Board DTOToEntity() {
        return Board.builder()
                .boardId(this.boardId)
                .boardType(this.boardType)
                .userId(
                        User.builder()
                                .userId(this.userId)
                                .build()
                )
                .moimId(Moim.builder()
                        .moimId(this.moimId)
                        .build()
                )
                .boardTitle(this.boardTitle)
                .boardContent(this.boardContent)
                .boardRegdate(this.boardRegdate)
                .boardUpdate(this.boardUpdate)
                .isPublic(this.isPublic)
                .build();
    }

}

package com.example.backend.dto;

import com.example.backend.entity.Board;
import com.example.backend.entity.Comment;
import com.example.backend.entity.Moim;
import com.example.backend.entity.User;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {

    private int commentId; //댓글Id
    private String userId;
    private int boardId; //게시판Id
    private String commentContent;
    private LocalDateTime commentRegdate;
    private LocalDateTime commentUpdate;

    private int moimId;



    public Comment DTOToEntity() {
        return Comment.builder()
                .commentId(this.commentId)
                .userId(
                        User.builder()
                                .userId(this.userId)
                                .build()
                )
                .boardId(
                        Board.builder()
                        .boardId(this.boardId)
                        .build()
                )
                .commentContent(this.commentContent)
                .commentRegdate(this.commentRegdate)
                .commentUpdate(this.commentUpdate)
                .build();
    }




}

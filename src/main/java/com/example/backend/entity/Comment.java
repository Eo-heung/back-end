package com.example.backend.entity;

import com.example.backend.dto.CommentDTO;
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
@Table(name = "COMMENT")
public class Comment {

    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentId; //댓글Id

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board boardId; //게시판Id

    @Column(name = "comment_content")
    private String commentContent;


    @Column(name = "comment_regdate")
    private LocalDateTime commentRegdate;

    @Column(name = "comment_update")
    private LocalDateTime commentUpdate;



    @PrePersist
    public void onPrePersist() {
        this.commentRegdate = LocalDateTime.now();
        this.commentUpdate = LocalDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate() {
        this.commentUpdate = LocalDateTime.now();
    }



    public CommentDTO EntityToDTO() {
        return CommentDTO.builder()
                .commentId(this.commentId)
                .userId(this.userId.getUserId())
                .boardId(this.boardId.getBoardId())
                .commentContent(this.commentContent)
                .commentRegdate(this.commentRegdate)
                .commentUpdate(this.commentUpdate)
                .build();
    }

    public CommentDTO toDTOWithMoim(int moimId, String userName) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCommentId(this.commentId);
        commentDTO.setUserId(this.userId.getUserId());
        commentDTO.setBoardId(this.boardId.getBoardId());
        commentDTO.setCommentContent(this.commentContent);
        commentDTO.setCommentRegdate(this.commentRegdate);
        commentDTO.setCommentUpdate(this.commentUpdate);
        commentDTO.setMoimId(moimId);
        commentDTO.setUserName(userName);
        return commentDTO;
    }



}
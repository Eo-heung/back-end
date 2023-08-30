package com.example.backend.repository;

import com.example.backend.entity.Board;
import com.example.backend.entity.Comment;
import com.example.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    //게시글 댓글 리스트
    Page<Comment> findByBoardId_BoardIdOrderByCommentRegdateDesc(int boardId, Pageable pageable);

    //모임 내 댓글 리스트
    @Query("SELECT c FROM Comment c WHERE c.userId = :user " +
            "AND c.boardId.moimId.moimId = :moimId " +
            "AND (c.commentContent LIKE CONCAT('%', :search, '%') OR :search IS NULL) " +
            "ORDER BY c.commentRegdate DESC")
    Page<Comment> findUserCommentsInMoim(@Param("user") User user, @Param("moimId") int moimId, @Param("search") String search, Pageable pageable);

    List<Comment> findByBoardId(Board board);



}

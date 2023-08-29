package com.example.backend.repository;

import com.example.backend.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Integer> {

    Page<Board> findByBoardType(Board.BoardType boardType, Pageable pageable);

    ////오름차순
    //전체조건
    @Query("SELECT b FROM Board b WHERE b.boardType = :boardType " +
            "AND (b.boardTitle LIKE CONCAT('%', :keyword, '%') OR b.boardContent LIKE CONCAT('%', :keyword, '%') " +
            "OR b.userId.userName LIKE CONCAT('%', :keyword, '%')) ORDER BY b.boardId ASC")
    Page<Board> searchByBoardTypeAndKeywordAsc(@Param("boardType") Board.BoardType boardType,
                                            @Param("keyword") String keyword, Pageable pageable);
    //제목
    Page<Board> findByBoardTypeAndBoardTitleContainingOrderByBoardIdAsc(Board.BoardType boardType, String keyword, Pageable pageable);
    //내용
    Page<Board> findByBoardTypeAndBoardContentContainingOrderByBoardIdAsc(Board.BoardType boardType, String keyword, Pageable pageable);
    //닉네임
    Page<Board> findByBoardTypeAndUserId_UserNameContainingOrderByBoardIdAsc(Board.BoardType boardType, String keyword, Pageable pageable);

    ////내림차순
    //전체조건
    @Query("SELECT b FROM Board b WHERE b.boardType = :boardType " +
            "AND (b.boardTitle LIKE CONCAT('%', :keyword, '%') OR b.boardContent LIKE CONCAT('%', :keyword, '%') " +
            "OR b.userId.userName LIKE CONCAT('%', :keyword, '%')) ORDER BY b.boardId DESC")
    Page<Board> searchByBoardTypeAndKeywordDesc(@Param("boardType") Board.BoardType boardType,
                                            @Param("keyword") String keyword, Pageable pageable);
    //제목
    Page<Board> findByBoardTypeAndBoardTitleContainingOrderByBoardIdDesc(Board.BoardType boardType, String keyword, Pageable pageable);
    //내용
    Page<Board> findByBoardTypeAndBoardContentContainingOrderByBoardIdDesc(Board.BoardType boardType, String keyword, Pageable pageable);
    //닉네임
    Page<Board> findByBoardTypeAndUserId_UserNameContainingOrderByBoardIdDesc(Board.BoardType boardType, String keyword, Pageable pageable);


}

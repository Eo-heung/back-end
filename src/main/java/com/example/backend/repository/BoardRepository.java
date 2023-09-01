package com.example.backend.repository;

import com.example.backend.entity.Board;
import com.example.backend.entity.Moim;
import com.example.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer> {

    ////오름차순
    //전체조건
    @Query("SELECT b FROM Board b WHERE b.boardType = :boardType AND b.moimId = :moimId " +
            "AND (b.boardTitle LIKE CONCAT('%', :keyword, '%') OR b.boardContent LIKE CONCAT('%', :keyword, '%') " +
            "OR b.userId.userName LIKE CONCAT('%', :keyword, '%')) ORDER BY b.boardId ASC")
    Page<Board> searchByBoardTypeAndKeywordAsc(@Param("boardType") Board.BoardType boardType,
                                               @Param("moimId") int moimId,
                                            @Param("keyword") String keyword, Pageable pageable);
    //제목
    @Query("SELECT b FROM Board b WHERE b.boardType = :boardType AND b.moimId.moimId = :moimId " +
            "AND b.boardTitle LIKE CONCAT('%', :keyword, '%') ORDER BY b.boardId ASC")
    Page<Board> findByBoardTypeAndMoimId_MoimIdAndBoardTitleContainingOrderByBoardIdAsc(Board.BoardType boardType, int moimId, String keyword, Pageable pageable);

    //내용
    @Query("SELECT b FROM Board b WHERE b.boardType = :boardType AND b.moimId.moimId = :moimId " +
            "AND b.boardContent LIKE CONCAT('%', :keyword, '%') ORDER BY b.boardId ASC")
    Page<Board> findByBoardTypeAndMoimId_MoimIdAndBoardContentContainingOrderByBoardIdAsc(Board.BoardType boardType, int moimId, String keyword, Pageable pageable);

    //닉네임
    @Query("SELECT b FROM Board b WHERE b.boardType = :boardType AND b.moimId.moimId = :moimId " +
            "AND b.userId.userName LIKE CONCAT('%', :keyword, '%') ORDER BY b.boardId ASC")
    Page<Board> findByBoardTypeAndMoimId_MoimIdAndUserId_UserNameContainingOrderByBoardIdAsc(Board.BoardType boardType, int moimId, String keyword, Pageable pageable);


    ////내림차순
    //전체조건
    @Query("SELECT b FROM Board b WHERE b.boardType = :boardType AND b.moimId = :moimId " +
            "AND (b.boardTitle LIKE CONCAT('%', :keyword, '%') OR b.boardContent LIKE CONCAT('%', :keyword, '%') " +
            "OR b.userId.userName LIKE CONCAT('%', :keyword, '%')) ORDER BY b.boardId DESC")
    Page<Board> searchByBoardTypeAndKeywordDesc(@Param("boardType") Board.BoardType boardType,
                                                @Param("moimId") int moimId,
                                            @Param("keyword") String keyword, Pageable pageable);
    //제목
    @Query("SELECT b FROM Board b WHERE b.boardType = :boardType AND b.moimId.moimId = :moimId " +
            "AND b.boardTitle LIKE CONCAT('%', :keyword, '%') ORDER BY b.boardId DESC")
    Page<Board> findByBoardTypeAndMoimId_MoimIdAndBoardTitleContainingOrderByBoardIdDesc(Board.BoardType boardType, int moimId, String keyword, Pageable pageable);

    //내용
    @Query("SELECT b FROM Board b WHERE b.boardType = :boardType AND b.moimId.moimId = :moimId " +
            "AND b.boardContent LIKE CONCAT('%', :keyword, '%') ORDER BY b.boardId DESC")
    Page<Board> findByBoardTypeAndMoimId_MoimIdAndBoardContentContainingOrderByBoardIdDesc(Board.BoardType boardType, int moimId, String keyword, Pageable pageable);

    //닉네임
    @Query("SELECT b FROM Board b WHERE b.boardType = :boardType AND b.moimId.moimId = :moimId " +
            "AND b.userId.userName LIKE CONCAT('%', :keyword, '%') ORDER BY b.boardId DESC")
    Page<Board> findByBoardTypeAndMoimId_MoimIdAndUserId_UserNameContainingOrderByBoardIdDesc(Board.BoardType boardType, int moimId, String keyword, Pageable pageable);



    ///내가 쓴 게시글
    Page<Board> findByUserIdOrderByBoardIdDesc(User user, Pageable pageable);
    Page<Board> findByUserIdAndBoardTitleContainingOrderByBoardIdDesc(User user, String keyword, Pageable pageable);
    Page<Board> findByUserIdAndBoardContentContainingOrderByBoardIdDesc(User user, String keyword, Pageable pageable);


    List<Board> findByMoimId(Moim moim);
}

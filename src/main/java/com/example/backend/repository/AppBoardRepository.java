package com.example.backend.repository;

import com.example.backend.entity.AppBoard;
import com.example.backend.entity.Board;
import com.example.backend.entity.Moim;
import com.example.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AppBoardRepository extends JpaRepository<AppBoard, Integer> {

    @Query("SELECT ab FROM AppBoard ab " +
            "JOIN ab.user u " +
            "WHERE u.userId = :loginUser " +
            "AND ab.moim.moimId = :moimId " +
            "AND (:appType IS NULL OR ab.appType = :appType) " +
            "AND (" +
            "     (:searchType = 'all' AND (ab.appTitle LIKE CONCAT('%', :keyword, '%') OR ab.appContent LIKE CONCAT('%', :keyword, '%'))) OR " +
            "     (:searchType = 'title' AND ab.appTitle LIKE CONCAT('%', :keyword, '%')) OR " +
            "     (:searchType = 'content' AND ab.appContent LIKE CONCAT('%', :keyword, '%'))" +
            ")")
    Page<AppBoard> findByUserAndMoimWithConditions(String loginUser, int moimId, AppBoard.AppType appType, String searchType,
                                                   String keyword, Pageable pageable);

    List<AppBoard> findByMoim(Moim moim);
}

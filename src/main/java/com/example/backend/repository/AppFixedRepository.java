package com.example.backend.repository;

import com.example.backend.entity.AppBoard;
import com.example.backend.entity.AppFixed;
import com.example.backend.entity.Moim;
import com.example.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppFixedRepository extends JpaRepository<AppFixed, Integer> {

    @Query("SELECT a FROM AppFixed a WHERE a.appFixedUser = :user AND a.appState = 'CONFIRM' AND " +
            "(a.appBoard.appStart BETWEEN :appStart AND :appEnd OR a.appBoard.appEnd BETWEEN :appStart AND :appEnd)")
    List<AppFixed> findOverlappingAppointments(@Param("user") User user, @Param("appStart") LocalDateTime appStart, @Param("appEnd") LocalDateTime appEnd);

    boolean existsByAppBoardAndAppFixedUser(AppBoard appBoard, User user);

    void deleteByAppBoard(AppBoard appBoard);

    List<AppFixed> findByAppBoard_Moim(Moim moim);


    @Query("SELECT a FROM AppFixed a WHERE a.appBoard.appBoardId= :appBoardId AND a.appState = :state ORDER BY CASE WHEN a.appSort = 'HOST' THEN 1 ELSE 2 END, a.appFixedId DESC")

    Page<AppFixed> findAllByAppBoardIdAndAppState(@Param("appBoardId") int appBoardId,  @Param("state") AppFixed.AppState state, Pageable pageable);



}

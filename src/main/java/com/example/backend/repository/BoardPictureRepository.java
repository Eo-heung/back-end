package com.example.backend.repository;

import com.example.backend.entity.Board;
import com.example.backend.entity.BoardPicture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardPictureRepository extends JpaRepository<BoardPicture, Integer> {
    List<BoardPicture> findByBoard(Board board);



}

package com.example.backend.repository;

import com.example.backend.entity.Board;
import com.example.backend.entity.BoardPicture;
import com.example.backend.entity.Moim;
import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardPictureRepository extends JpaRepository<BoardPicture, Integer> {

    List<BoardPicture> findByBoard(Board board);
}

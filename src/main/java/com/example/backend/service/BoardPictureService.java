package com.example.backend.service;

import com.example.backend.dto.BoardDTO;
import com.example.backend.entity.Board;
import com.example.backend.entity.BoardPicture;
import com.example.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardPictureService {
    List<BoardPicture> viewBoardPic(Board board);


}

package com.example.backend.service;

import com.example.backend.entity.Board;
import com.example.backend.entity.BoardPicture;
import java.util.List;

public interface BoardPictureService {
    List<BoardPicture> viewBoardPic(Board board);



}

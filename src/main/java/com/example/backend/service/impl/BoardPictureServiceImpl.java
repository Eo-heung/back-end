package com.example.backend.service.impl;

import com.example.backend.entity.*;
import com.example.backend.repository.BoardPictureRepository;
import com.example.backend.repository.BoardRepository;
import com.example.backend.repository.MoimRegistrationRepository;
import com.example.backend.repository.MoimRepository;
import com.example.backend.service.BoardPictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardPictureServiceImpl implements BoardPictureService {
    private final BoardPictureRepository boardPictureRepository;

    @Override
    public List<BoardPicture> viewBoardPic(Board board) {
        return boardPictureRepository.findByBoard(board);
    }



}

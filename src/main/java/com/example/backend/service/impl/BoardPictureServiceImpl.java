package com.example.backend.service.impl;

import com.example.backend.dto.BoardDTO;
import com.example.backend.entity.*;
import com.example.backend.repository.BoardPictureRepository;
import com.example.backend.repository.BoardRepository;
import com.example.backend.repository.MoimRegistrationRepository;
import com.example.backend.repository.MoimRepository;
import com.example.backend.service.BoardPictureService;
import com.example.backend.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BoardPictureServiceImpl implements BoardPictureService {
    private final BoardRepository boardRepository;
    private final BoardPictureRepository boardPictureRepository;
    private final MoimRegistrationRepository moimRegistrationRepository;
    private final MoimRepository moimRepository;


    @Override
    public List<BoardPicture> viewBoardPic(Board boardId) {
        return boardPictureRepository.findByBoardId(boardId);
    }
}

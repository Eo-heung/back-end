package com.example.backend.service;

import com.example.backend.dto.BoardDTO;
import com.example.backend.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface BoardService {
//    FreeBoard viewMoim(int moimId);

    Board createBoard(User loginUser, Board board, List<BoardPicture> boardPic, int moimId);

    Page<BoardDTO> getNoticeBoard(User loginUser, Pageable pageable, int moimId);

    Page<BoardDTO> getFreeBoard(User loginUser,Pageable pageable, int moimId);

    Board viewboard(User loginUser, int boardId, int moimId);

    Board modifyBoard(User loginUser, BoardDTO boardDTO, List<MultipartFile> newPictures, List<Integer> deletePictureIds,
                      Map<Integer, MultipartFile> updatePicturesMap, int moimId) throws IOException;

    public boolean verifyMemberRole(User user, Moim moim);
    public boolean verifyLeaderRole(User user, Moim moim);

    }

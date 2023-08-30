package com.example.backend.service;

import com.example.backend.dto.BoardDTO;
import com.example.backend.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface BoardService {
    Board createBoard(User loginUser, Board board, List<BoardPicture> boardPic, int moimId);

    Page<BoardDTO> getNoticeBoard(User loginUser, Pageable pageable, int moimId, String keyword, String searchType, String orderBy);

    Page<BoardDTO> getFreeBoard(User loginUser, Pageable pageable, int moimId, String keyword, String searchType, String orderBy);

    Board viewboard(User loginUser, int boardId, int moimId);

    Board modifyBoard(int boardId,
                      User loginUser,
                      Board.BoardType boardType,
                      String boardTitle,
                      String boardContent,
                      List<MultipartFile> newPictures,
                      List<Integer> deletePictureIds,
                      List<MultipartFile> updatePictures,
                      int moimId) throws IOException;

    public boolean verifyMemberRole(User user, Moim moim);
    public boolean verifyLeaderRole(User user, Moim moim);

    }

package com.example.backend.service;

import com.example.backend.dto.BoardDTO;
import com.example.backend.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BoardService {
    Board createBoard(User loginUser, Board board, List<BoardPicture> boardPic, int moimId);

    Page<BoardDTO> getNoticeBoard(User loginUser, Pageable pageable, int moimId, String keyword, String searchType, String orderBy);

    Page<BoardDTO> getFreeBoard(User loginUser, Pageable pageable, int moimId, String keyword, String searchType, String orderBy);

    Page<BoardDTO> getMyBoard(int moimId, User loginUser, Pageable pageable, String keyword, String searchType);

    Board viewboard(User loginUser, int boardId, int moimId);

    Board modifyBoard(int boardId, User loginUser, Board.BoardType boardType, String boardTitle, String boardContent,
                       MultipartFile[] updatePictures, int moimId) throws IOException;

    String deleteBoard(User loginuser, int boardId, Moim moim) throws Exception;

    public boolean verifyMemberRole(User user, Moim moim);

    public boolean verifyLeaderRole(User user, Moim moim);



}

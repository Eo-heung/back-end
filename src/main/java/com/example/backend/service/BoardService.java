package com.example.backend.service;

import com.example.backend.dto.BoardDTO;
import com.example.backend.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardService {
//    FreeBoard viewMoim(int moimId);

    Board createBoard(User loginUser, Board board, List<BoardPicture> boardPic, int moimId);

    Page<BoardDTO> getNoticeBoard(User loginUser, Pageable pageable, int moimId);

    Page<BoardDTO> getFreeBoard(User loginUser,Pageable pageable, int moimId);

    Board viewboard(User loginUser, int boardId, int moimId);





//    FreeBoard modifyMoim(Moim moim);

//    List<FreeBoard> getMoimList();

//    Page<FreeBoard> searchMoims(User user, String category, String keyword, String searchType, String orderBy, Pageable pageable);

}

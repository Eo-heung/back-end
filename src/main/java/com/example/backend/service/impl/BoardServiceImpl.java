package com.example.backend.service.impl;

import com.example.backend.dto.BoardAndPictureDTO;
import com.example.backend.dto.BoardDTO;
import com.example.backend.entity.*;
import com.example.backend.repository.BoardPictureRepository;
import com.example.backend.repository.BoardRepository;
import com.example.backend.repository.MoimRegistrationRepository;
import com.example.backend.repository.MoimRepository;
import com.example.backend.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final BoardPictureRepository boardPictureRepository;
    private final MoimRegistrationRepository moimRegistrationRepository;
    private final MoimRepository moimRepository;

    @Override
    public Board createBoard(User loginUser, Board board, List<BoardPicture> boardPics, int moimId) {

        Moim checkMoim = moimRepository.findById(moimId)
                .orElseThrow(() -> new NoSuchElementException("Moim not found"));

        Board savedBoard;

        if (loginUser.equals(checkMoim.getUserId())) {
            savedBoard = boardRepository.save(board);
        } else {
            if (isUserAMemberOfMoim(loginUser, checkMoim)) {
                if (board.getBoardType().equals(Board.BoardType.FREE)) {
                    savedBoard = boardRepository.save(board);
                } else {
                    throw new IllegalStateException("모임원은 자유 게시판에만 게시할 수 있습니다.");
                }
            } else {
                throw new IllegalStateException("이 사용자는 이 모임에 승인되지 않았습니다.");
            }
        }

        if (boardPics != null && !boardPics.isEmpty()) {
            for (BoardPicture boardPic : boardPics) {
                boardPic.setBoardId(savedBoard);
                boardPictureRepository.save(boardPic);
            }
        }

        return savedBoard;
    }


    public Page<BoardDTO> getNoticeBoard(User loginUser, Pageable pageable, int moimId) {
        Moim checkMoim = moimRepository.findById(moimId)
                .orElseThrow(() -> new NoSuchElementException("Moim not found"));

        if (loginUser.equals(checkMoim.getUserId()) || isUserAMemberOfMoim(loginUser, checkMoim)) {
            Page<Board> noticeList = boardRepository.findByBoardType(Board.BoardType.NOTICE, pageable);
            return noticeList.map(board -> BoardDTO.builder()
                    .boardId(board.getBoardId())
                    .boardType(board.getBoardType())
                    .userId(board.getUserId().getUserId())
                    .userName(board.getUserId().getUserName())
                    .moimId(moimId)
                    .boardTitle(board.getBoardTitle())
                    .boardContent(board.getBoardContent())
                    .boardRegdate(board.getBoardRegdate())
                    .build());
        } else {
            throw new IllegalStateException("이 사용자는 이 모임에 승인되지 않았습니다.");
        }
    }

    public Page<BoardDTO> getFreeBoard(User loginUser, Pageable pageable, int moimId) {
        Moim checkMoim = moimRepository.findById(moimId)
                .orElseThrow(() -> new NoSuchElementException("Moim not found"));

        if (loginUser.equals(checkMoim.getUserId()) || isUserAMemberOfMoim(loginUser, checkMoim)) {
            Page<Board> freeList = boardRepository.findByBoardType(Board.BoardType.FREE, pageable);
            return freeList.map(board -> BoardDTO.builder()
                    .boardId(board.getBoardId())
                    .boardType(board.getBoardType())
                    .userId(board.getUserId().getUserId())
                    .userName(board.getUserId().getUserName())
                    .moimId(moimId)
                    .boardTitle(board.getBoardTitle())
                    .boardContent(board.getBoardContent())
                    .boardRegdate(board.getBoardRegdate())
                    .build());
        } else {
            throw new IllegalStateException("이 사용자는 이 모임에 승인되지 않았습니다.");
        }
    }

    @Override
    public Board viewboard(User loginUser,int boardId, int moimId) {
        Moim checkMoim = moimRepository.findById(moimId)
                .orElseThrow(() -> new NoSuchElementException("Moim not found"));

        Optional<Board> optionalBoard = boardRepository.findById(boardId);

        if(optionalBoard.isEmpty()) {
            throw new NoSuchElementException("Board not found");
        }

        Board board = optionalBoard.get();
        Moim relatedMoim = board.getMoimId();

        if (loginUser.equals(relatedMoim.getUserId()) || isUserAMemberOfMoim(loginUser, relatedMoim)) {
            return board;
        } else {
            throw new IllegalStateException("이 사용자는 이 게시물을 보는 권한이 없습니다.");
        }
    }

    private boolean isUserAMemberOfMoim(User user, Moim moim) {
        MoimRegistration registration = moimRegistrationRepository.findByMoimAndUser(moim, user)
                .orElseThrow(() -> new NoSuchElementException("이 모임에 등록되지 않은 사용자입니다."));
        return registration.getRegStatus() == MoimRegistration.RegStatus.APPROVED;
    }








}

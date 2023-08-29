package com.example.backend.service.impl;

import com.example.backend.dto.BoardAndPictureDTO;
import com.example.backend.dto.BoardDTO;
import com.example.backend.entity.*;
import com.example.backend.repository.*;
import com.example.backend.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.lang.IllegalStateException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final BoardPictureRepository boardPictureRepository;
    private final MoimRegistrationRepository moimRegistrationRepository;
    private final MoimRepository moimRepository;
    private final UserRepository userRepository;

    @Override
    public Board createBoard(User loginUser, Board board, List<BoardPicture> boardPics, int moimId) {

        Moim checkMoim = moimRepository.findById(moimId)
                .orElseThrow(() -> new NoSuchElementException("Moim not found"));

        Board savedBoard;

        if (loginUser.equals(checkMoim.getUserId())) { //모임장 구분
            savedBoard = boardRepository.save(board);
        } else {
            if (isUserAMemberOfMoim(loginUser, checkMoim)) { //모임원 구분
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
                boardPic.setBoard(savedBoard);
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


    public Board modifyBoard(User loginUser,
                             BoardDTO boardDTO,
                             List<MultipartFile> newPictures,
                             List<Integer> deletePictureIds,
                             Map<Integer, MultipartFile> updatePicturesMap,
                             int moimId) throws IOException {

        Board board = toEntity(boardDTO);

        // 게시글을 수정하기 전에 권한을 체크
        Board existingBoard = boardRepository.findById(board.getBoardId())
                .orElseThrow(() -> new NoSuchElementException("Board not found"));

        if (!loginUser.equals(existingBoard.getUserId())) {
            throw new IllegalStateException("글쓴이만 수정할 수 있습니다.");
        }

        Moim checkMoim = moimRepository.findById(moimId)
                .orElseThrow(() -> new NoSuchElementException("Moim not found"));

        if (board.getBoardType().equals(Board.BoardType.FREE)) {
            if (!isUserAMemberOfMoim(loginUser, checkMoim) && !loginUser.equals(checkMoim.getUserId())) {
                throw new IllegalStateException("모임원이나 모임장만 수정할 수 있습니다.");
            }
        } else if (!loginUser.equals(checkMoim.getUserId())) {
            throw new IllegalStateException("모임장만 공지 게시판을 수정할 수 있습니다.");
        }

        existingBoard.setBoardTitle(board.getBoardTitle());
        existingBoard.setBoardContent(board.getBoardContent());
        existingBoard.setBoardType(board.getBoardType());
        existingBoard.setPublic(board.isPublic());
        existingBoard.setBoardRegdate(board.getBoardRegdate());
        existingBoard.setBoardUpdate(LocalDateTime.now());

        boardRepository.save(existingBoard);

        // 기존 사진 삭제
        if (deletePictureIds != null && !deletePictureIds.isEmpty()) {
            for (Integer picId : deletePictureIds) {
                boardPictureRepository.deleteById(picId);
            }
        }

        // 기존 사진 수정
        if (updatePicturesMap != null && !updatePicturesMap.isEmpty()) {
            for (Map.Entry<Integer, MultipartFile> entry : updatePicturesMap.entrySet()) {
                BoardPicture existingPicture = boardPictureRepository.findById(entry.getKey())
                        .orElseThrow(() -> new NoSuchElementException("Picture not found "));

                byte[] picBytes = entry.getValue().getBytes();
                existingPicture.setBoardPic(picBytes);
                existingPicture.setUpdateBoardPic(LocalDateTime.now());

                boardPictureRepository.save(existingPicture);
            }
        }

        // 새로운 사진 추가
        if (newPictures != null && !newPictures.isEmpty()) {
            for (MultipartFile file : newPictures) {
                byte[] picBytes = file.getBytes();
                BoardPicture boardPicture = BoardPicture.builder()
                        .userId(loginUser)
                        .moimId(Moim.builder().moimId(moimId).build())
                        .boardPic(picBytes)
                        .createBoardPic(LocalDateTime.now())
                        .board(existingBoard)
                        .build();
                boardPictureRepository.save(boardPicture);
            }
        }

        return existingBoard;
    }


    private Board toEntity(BoardDTO boardDTO) {
        Board board = new Board();
        board.setBoardId(boardDTO.getBoardId());
        board.setBoardType(boardDTO.getBoardType());
        board.setBoardTitle(boardDTO.getBoardTitle());
        board.setBoardContent(boardDTO.getBoardContent());
        board.setBoardRegdate(boardDTO.getBoardRegdate());
        board.setBoardUpdate(boardDTO.getBoardUpdate());
        board.setPublic(boardDTO.isPublic());

        User user = userRepository.findByUserId(boardDTO.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + boardDTO.getUserId()));

        board.setUserId(user);

        return board;
    }


    private boolean isUserAMemberOfMoim(User user, Moim moim) {
        MoimRegistration registration = moimRegistrationRepository.findByMoimAndUser(moim, user)
                .orElseThrow(() -> new NoSuchElementException("이 모임에 등록되지 않은 사용자입니다."));
        return registration.getRegStatus() == MoimRegistration.RegStatus.APPROVED;
    }

    public boolean verifyMemberRole(User user, Moim moim) {
        Optional<MoimRegistration> registrationOpt = moimRegistrationRepository.findByMoimAndUser(moim, user);
        if (registrationOpt.isPresent()) {
            return registrationOpt.get().getRegStatus() == MoimRegistration.RegStatus.APPROVED;
        } else {
            return false;
        }
    }

    public boolean verifyLeaderRole(User user, Moim moim) {
        return user.equals(moim.getUserId());
    }



}

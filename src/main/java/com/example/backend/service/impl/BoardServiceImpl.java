package com.example.backend.service.impl;

import com.example.backend.dto.BoardDTO;
import com.example.backend.entity.*;
import com.example.backend.repository.*;
import com.example.backend.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.lang.IllegalStateException;
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
    private final CommentRepository commentRepository;

    //게시글 등록
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

    //공지게시판 리스트
    @Override
    public Page<BoardDTO> getNoticeBoard(User loginUser, Pageable pageable, int moimId, String keyword, String searchType, String orderBy) {
        Moim checkMoim = moimRepository.findById(moimId)
                .orElseThrow(() -> new NoSuchElementException("Moim not found"));

        if (loginUser.equals(checkMoim.getUserId()) || isUserAMemberOfMoim(loginUser, checkMoim)) {
            Page<Board> noticeList;

            switch (searchType) {
                case "title":
                    if ("ascending".equals(orderBy)) {
                        noticeList = boardRepository.findByBoardTypeAndMoimId_MoimIdAndBoardTitleContainingOrderByBoardIdAsc(Board.BoardType.NOTICE, moimId, keyword, pageable);
                    } else {
                        noticeList = boardRepository.findByBoardTypeAndMoimId_MoimIdAndBoardTitleContainingOrderByBoardIdDesc(Board.BoardType.NOTICE, moimId, keyword, pageable);
                    }
                    break;
                case "content":
                    if ("ascending".equals(orderBy)) {
                        noticeList = boardRepository.findByBoardTypeAndMoimId_MoimIdAndBoardContentContainingOrderByBoardIdAsc(Board.BoardType.NOTICE, moimId, keyword, pageable);
                    } else {
                        noticeList = boardRepository.findByBoardTypeAndMoimId_MoimIdAndBoardContentContainingOrderByBoardIdDesc(Board.BoardType.NOTICE, moimId, keyword, pageable);
                    }
                    break;
                case "nickname":
                    if ("ascending".equals(orderBy)) {
                        noticeList = boardRepository.findByBoardTypeAndMoimId_MoimIdAndUserId_UserNameContainingOrderByBoardIdAsc(Board.BoardType.NOTICE, moimId, keyword, pageable);
                    } else {
                        noticeList = boardRepository.findByBoardTypeAndMoimId_MoimIdAndUserId_UserNameContainingOrderByBoardIdDesc(Board.BoardType.NOTICE, moimId, keyword, pageable);
                    }
                    break;
                default:
                    if ("ascending".equals(orderBy)) {
                        noticeList = boardRepository.searchByBoardTypeAndKeywordAsc(Board.BoardType.NOTICE, moimId, keyword, pageable);
                    } else {
                        noticeList = boardRepository.searchByBoardTypeAndKeywordDesc(Board.BoardType.NOTICE, moimId, keyword, pageable);
                    }
                    break;
            }

            return noticeList.map(board -> {
                int commentCount = commentRepository.countByBoardId_BoardId(board.getBoardId());
                return BoardDTO.builder()
                        .boardId(board.getBoardId())
                        .boardType(board.getBoardType())
                        .userId(board.getUserId().getUserId())
                        .userName(board.getUserId().getUserName())
                        .moimId(moimId)
                        .boardTitle(board.getBoardTitle())
                        .boardContent(board.getBoardContent())
                        .boardRegdate(board.getBoardRegdate())
                        .commentCnt(commentCount)  // 댓글 갯수 추가
                        .build();
            });

        } else {
            throw new IllegalStateException("이 사용자는 이 모임에 승인되지 않았습니다.");
        }
    }

    //자유게시판 리스트
    @Override
    public Page<BoardDTO> getFreeBoard(User loginUser, Pageable pageable, int moimId, String keyword, String searchType, String orderBy) {
            Moim checkMoim = moimRepository.findById(moimId)
                    .orElseThrow(() -> new NoSuchElementException("Moim not found"));

            if (loginUser.equals(checkMoim.getUserId()) || isUserAMemberOfMoim(loginUser, checkMoim)) {
                Page<Board> freeList;

                switch (searchType) {
                    case "title":
                        if ("ascending".equals(orderBy)) {
                            freeList = boardRepository.findByBoardTypeAndMoimId_MoimIdAndBoardTitleContainingOrderByBoardIdAsc(Board.BoardType.FREE, moimId, keyword, pageable);
                        } else {
                            freeList = boardRepository.findByBoardTypeAndMoimId_MoimIdAndBoardTitleContainingOrderByBoardIdDesc(Board.BoardType.FREE, moimId, keyword, pageable);
                        }
                        break;
                    case "content":
                        if ("ascending".equals(orderBy)) {
                            freeList = boardRepository.findByBoardTypeAndMoimId_MoimIdAndBoardContentContainingOrderByBoardIdAsc(Board.BoardType.FREE, moimId, keyword, pageable);
                        } else {
                            freeList = boardRepository.findByBoardTypeAndMoimId_MoimIdAndBoardContentContainingOrderByBoardIdDesc(Board.BoardType.FREE, moimId, keyword, pageable);
                        }
                        break;
                    case "nickname":
                        if ("ascending".equals(orderBy)) {
                            freeList = boardRepository.findByBoardTypeAndMoimId_MoimIdAndUserId_UserNameContainingOrderByBoardIdAsc(Board.BoardType.FREE, moimId, keyword, pageable);
                        } else {
                            freeList = boardRepository.findByBoardTypeAndMoimId_MoimIdAndUserId_UserNameContainingOrderByBoardIdDesc(Board.BoardType.FREE, moimId, keyword, pageable);
                        }
                        break;
                    default:
                        if ("ascending".equals(orderBy)) {
                            freeList = boardRepository.searchByBoardTypeAndKeywordAsc(Board.BoardType.FREE, moimId, keyword, pageable);
                        } else {
                            freeList = boardRepository.searchByBoardTypeAndKeywordDesc(Board.BoardType.FREE, moimId, keyword, pageable);
                        }
                        break;
                }

                return freeList.map(board -> {
                    int commentCount = commentRepository.countByBoardId_BoardId(board.getBoardId());
                    return BoardDTO.builder()
                            .boardId(board.getBoardId())
                            .boardType(board.getBoardType())
                            .userId(board.getUserId().getUserId())
                            .userName(board.getUserId().getUserName())
                            .moimId(moimId)
                            .boardTitle(board.getBoardTitle())
                            .boardContent(board.getBoardContent())
                            .boardRegdate(board.getBoardRegdate())
                            .commentCnt(commentCount)  // 댓글 갯수 추가
                            .build();
                });
            } else {
                throw new IllegalStateException("이 사용자는 이 모임에 승인되지 않았습니다.");
            }
    }

    //내 게시글
    @Override
    public Page<BoardDTO> getMyBoard(User loginUser, Pageable pageable, String keyword, String searchType) {
        Page<Board> userBoards;

        switch (searchType) {
            case "title":
                userBoards = boardRepository.findByUserIdAndBoardTitleContainingOrderByBoardIdDesc(loginUser, keyword, pageable);
                break;
            case "content":
                userBoards = boardRepository.findByUserIdAndBoardContentContainingOrderByBoardIdDesc(loginUser, keyword, pageable);
                break;
            default:
                userBoards = boardRepository.findByUserIdOrderByBoardIdDesc(loginUser, pageable);
                break;
        }

        return userBoards.map(board -> {
            int commentCount = commentRepository.countByBoardId_BoardId(board.getBoardId());
            int currentMoimId = board.getMoimId().getMoimId();
            return BoardDTO.builder()
                    .boardId(board.getBoardId())
                    .boardType(board.getBoardType())
                    .userId(board.getUserId().getUserId())
                    .userName(board.getUserId().getUserName())
                    .moimId(currentMoimId)
                    .boardTitle(board.getBoardTitle())
                    .boardContent(board.getBoardContent())
                    .boardRegdate(board.getBoardRegdate())
                    .commentCnt(commentCount)  // 댓글 갯수 추가
                    .build();
        });

    }

    //게시글 상세보기
    @Override
    public Board viewboard(User loginUser, int boardId, int moimId) {
        Moim checkMoim = moimRepository.findById(moimId)
                .orElseThrow(() -> new NoSuchElementException("Moim not found"));

        Optional<Board> optionalBoard = boardRepository.findById(boardId);

        if (optionalBoard.isEmpty()) {
            throw new NoSuchElementException("Board not found");
        }

        Board board = optionalBoard.get();

        if (loginUser.equals(checkMoim.getUserId()) || isUserAMemberOfMoim(loginUser, checkMoim)) {
            return board;
        } else {
            throw new IllegalStateException("이 사용자는 이 게시물을 보는 권한이 없습니다.");
        }
    }

    //게시글 수정
    @Transactional
    @Override
    public Board modifyBoard(int boardId,
                             User loginUser,
                             Board.BoardType boardType,
                             String boardTitle,
                             String boardContent,
                             MultipartFile[] updatePictures,
                             int moimId) throws IOException {


        // 게시글을 수정하기 전에 권한을 체크
        Board existingBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException("Board not found"));

        if (!loginUser.equals(existingBoard.getUserId())) {
            throw new IllegalStateException("글쓴이만 수정할 수 있습니다.");
        }

        Moim checkMoim = moimRepository.findById(moimId)
                .orElseThrow(() -> new NoSuchElementException("Moim not found"));

        if (boardType.equals(Board.BoardType.FREE)) {
            if (!isUserAMemberOfMoim(loginUser, checkMoim) && !loginUser.equals(checkMoim.getUserId())) {
                throw new IllegalStateException("모임원이나 모임장만 수정할 수 있습니다.");
            }
        } else if (!loginUser.equals(checkMoim.getUserId())) {
            throw new IllegalStateException("모임장만 공지 게시판을 수정할 수 있습니다.");
        }

        existingBoard.setUserId(loginUser);
        existingBoard.setBoardType(boardType);
        existingBoard.setBoardTitle(boardTitle);
        existingBoard.setBoardContent(boardContent);
        existingBoard.setBoardUpdate(LocalDateTime.now());

        boardRepository.save(existingBoard);


        boardPictureRepository.deleteAllByBoard(Board.builder()
                                                     .boardId(boardId)
                                                     .build());

        // 새로운 사진 추가
        if (updatePictures != null ) {
            for (MultipartFile file : updatePictures) {
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

    //게시글 삭제. 반환 메세지 넘기기 위해 String deleteBoard로 받음
    public String deleteBoard(User loginuser, int boardId, Moim moim) throws Exception {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new Exception("게시글을 찾을 수 없습니다"));

        if (!board.getUserId().equals(loginuser)) {
            throw new IllegalAccessException("게시글 삭제 권한이 없습니다.");
        }

        // 게시글에 연관된 댓글들 삭제
        List<Comment> comments = commentRepository.findByBoardId(board);
        commentRepository.deleteAll(comments);

        // 게시글 연관된 사진 삭제
        List<BoardPicture> boardPictures = boardPictureRepository.findByBoard(board);
        boardPictureRepository.deleteAll(boardPictures);

        // 게시글 삭제
        boardRepository.delete(board);

        // 게시글 작성자와 로그인한 사용자가 같고, 로그인한 사용자가 모임장일 경우
        if (board.getUserId().equals(loginuser) && verifyLeaderRole(loginuser, moim)) {
            return "모임장이 게시글을 삭제했습니다.";
        }
        return "게시글이 정상적으로 삭제되었습니다.";
    }



    private boolean isUserAMemberOfMoim(User user, Moim moim) {

        Optional<MoimRegistration> optionalRegistration = moimRegistrationRepository.findByMoimAndUser(moim, user);

        if (!optionalRegistration.isPresent()) {
            return false; // 모임에 등록되지 않은 사용자
        }

        MoimRegistration registration = optionalRegistration.get();
        return registration.getRegStatus() == MoimRegistration.RegStatus.APPROVED;
    }

    public boolean verifyMemberRole(User user, Moim moim) {

        Optional<MoimRegistration> optionalRegistration = moimRegistrationRepository.findByMoimAndUser(moim, user);

        if (!optionalRegistration.isPresent()) {
            return false; // 모임에 등록되지 않은 사용자
        }

        MoimRegistration registration = optionalRegistration.get();
        return registration.getRegStatus() == MoimRegistration.RegStatus.APPROVED;
    }

    public boolean verifyLeaderRole(User user, Moim moim) {
        return user.equals(moim.getUserId());
    }



}

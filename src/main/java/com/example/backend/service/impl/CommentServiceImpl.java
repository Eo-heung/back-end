package com.example.backend.service.impl;

import com.example.backend.entity.*;
import com.example.backend.repository.*;
import com.example.backend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;
import org.springframework.security.access.AccessDeniedException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final MoimRegistrationRepository moimRegistrationRepository;
    private final MoimRepository moimRepository;
    private final BoardRepository boardRepository;

    //댓글 작성
    @Override
    public Comment createComment(int moimId, int boardId, String content, User loginUser) {
        Moim checkMoim = moimRepository.findById(moimId)
                .orElseThrow(() -> new NoSuchElementException("[Comment Service] 모임을 찾을 수 없습니다."));

        Board checkBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException("[Comment Service] 게시글을 찾을 수 없습니다."));

        if (checkBoard.getBoardType() == Board.BoardType.NOTICE) {
            throw new UnsupportedOperationException("[Comment Service] 공지사항에는 댓글을 달 수 없습니다.");
        }

        if (!verifyMemberRole(loginUser, checkMoim) && !verifyLeaderRole(loginUser, checkMoim)) {
            throw new IllegalStateException("[Comment Service] 댓글 권한이 없습니다.");
        }

        Comment comment = Comment.builder()
                .userId(loginUser)
                .boardId(checkBoard)
                .commentContent(content)
                .build();
        return commentRepository.save(comment);
    }

    //댓글 수정
    public Comment modifyComment(int moimId, int boardId, int commentId, String content, User loginUser) {
        Comment checkComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("[Comment Service] 댓글을 찾을 수 없습니다."));

        Moim checkMoim = moimRepository.findById(moimId)
                .orElseThrow(() -> new NoSuchElementException("[Comment Service] 모임을 찾을 수 없습니다."));

        Board checkBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException("[Comment Service] 게시글을 찾을 수 없습니다."));

        if (checkBoard.getBoardType() == Board.BoardType.NOTICE) {
            throw new UnsupportedOperationException("[Comment Service] 공지사항에는 댓글을 달 수 없습니다.");
        }

        if (!checkComment.getUserId().equals(loginUser)) {
            throw new AccessDeniedException("[Comment Service] 댓글 작성자만 수정 가능합니다.");
        }

        checkComment.setCommentContent(content);
        return commentRepository.save(checkComment);
    }

    //댓글 삭제(댓글 작성자, 모임장)
    public Comment deleteComment(int moimId, int boardId, int commentId, User loginUser) {
        Comment checkComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("[Comment Service] 댓글을 찾을 수 없습니다."));

        Moim checkMoim = moimRepository.findById(moimId)
                .orElseThrow(() -> new NoSuchElementException("[Comment Service] 모임을 찾을 수 없습니다."));

        boolean isCommentOwner = checkComment.getUserId().equals(loginUser);
        boolean isMoimLeader = verifyLeaderRole(loginUser, checkMoim);

        if (!(isCommentOwner || isMoimLeader)) {
            throw new AccessDeniedException("[Comment Service] 댓글은 댓글작성자나 모임장만 삭제 가능합니다.");
        }

        commentRepository.deleteById(commentId);
        return checkComment;

    }

    //게시글 당 댓글 리스트
    public Page<Comment> getComment(int moimId, int boardId, Pageable pageable, User loginUser) {
        Moim checkMoim = moimRepository.findById(moimId)
                .orElseThrow(() -> new NoSuchElementException("[Comment Service] 모임을 찾을 수 없습니다."));

        return commentRepository.findByBoardId_BoardIdOrderByCommentRegdateDesc(boardId, pageable);
    }

    //모임 내 내가 작성한 댓글 리스트
    public Page<Comment> getMyComment(User loginUser, int moimId, Pageable pageable, String keyword) {
        Moim checkMoim = moimRepository.findById(moimId)
                .orElseThrow(() -> new NoSuchElementException("[Comment Service] 모임을 찾을 수 없습니다."));

        boolean isMember = verifyMemberRole(loginUser, checkMoim);
        boolean isLeader = verifyLeaderRole(loginUser, checkMoim);

        if (!(isMember || isLeader)) {
            throw new AccessDeniedException("[Comment Service] 내 댓글 목록은 본인만 확인할 수 있습니다.");
        }


        return commentRepository.findUserCommentsInMoim(loginUser, moimId, keyword, pageable);
    }



    //모임원 여부 확인(boolean)
    public boolean verifyMemberRole(User user, Moim moim) {
        Optional<MoimRegistration> optionalRegistration = moimRegistrationRepository.findByMoimAndUser(moim, user);

        if (!optionalRegistration.isPresent()) {
            return false; // 모임에 등록되지 않은 사용자
        }

        MoimRegistration registration = optionalRegistration.get();
        return registration.getRegStatus() == MoimRegistration.RegStatus.APPROVED;
    }

    //모임장 여부 확인(boolean)
    public boolean verifyLeaderRole(User user, Moim moim) {
        return user.equals(moim.getUserId());
    }



}

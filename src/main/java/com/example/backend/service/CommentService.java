package com.example.backend.service;

import com.example.backend.dto.BoardDTO;
import com.example.backend.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CommentService {
    public Comment createComment(int moimId, int boardId, String content, User loginUser);

    Comment modifyComment(int moimId, int boardId, int commentId, String content, User loginUser);

    Comment deleteComment(int moimId, int boardId, int commentId, User loginUser);

    Page<Comment> getComment(int moimId, int boardId, Pageable pageable, User loginUser);

    Page<Comment> getMyComment(User loginUser, int moimId, Pageable pageable);

    public boolean verifyMemberRole(User user, Moim moim);
    public boolean verifyLeaderRole(User user, Moim moim);

    }

package com.example.backend.controller;

import com.example.backend.dto.BoardDTO;
import com.example.backend.dto.CommentDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.entity.*;
import com.example.backend.jwt.JwtTokenProvider;
import com.example.backend.repository.*;
import com.example.backend.service.BoardPictureService;
import com.example.backend.service.BoardService;
import com.example.backend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @PostMapping("/{moimId}/{boardId}/create-co")
    public ResponseEntity<?> createComment(@PathVariable("moimId") int moimId,
                                           @PathVariable("boardId") int boardId,
                                           @RequestParam String content,
                                           @RequestHeader("Authorization") String token) {

        ResponseDTO<CommentDTO> responseDTO = new ResponseDTO<>();

        try {
            String loggedInUsername = jwtTokenProvider.validateAndGetUsername(token);
            User loginUser = userRepository.findByUserId(loggedInUsername)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new NoSuchElementException("Board not found"));

            Comment comment = commentService.createComment(moimId, boardId, content, loginUser);
            CommentDTO commentDTO = comment.toDTOWithMoim(moimId);

            responseDTO.setItem(commentDTO);
            responseDTO.setStatusCode(HttpStatus.CREATED.value());

            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    @PutMapping("/{moimId}/{boardId}/modify-co/{commentId}")
    public ResponseEntity<?> modifyComment(@PathVariable("moimId") int moimId,
                                           @PathVariable("boardId") int boardId,
                                           @PathVariable("commentId") int commentId,
                                           @RequestParam String content,
                                           @RequestHeader("Authorization") String token) {

        String loggedInUsername = jwtTokenProvider.validateAndGetUsername(token);
        User loginUser = userRepository.findByUserId(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ResponseDTO<CommentDTO> responseDTO = new ResponseDTO<>();

        try {
            Comment updatedComment = commentService.modifyComment(moimId, boardId, commentId, content, loginUser);
            CommentDTO commentDTO = updatedComment.toDTOWithMoim(moimId);

            responseDTO.setItem(commentDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @DeleteMapping("/{moimId}/{boardId}/delete-co/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("moimId") int moimId,
                                           @PathVariable("boardId") int boardId,
                                           @PathVariable("commentId") int commentId,
                                           @RequestHeader("Authorization") String token) {
        ResponseDTO<String> responseDTO = new ResponseDTO<>();

        String loggedInUsername = jwtTokenProvider.validateAndGetUsername(token);
        User loginUser = userRepository.findByUserId(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            commentService.deleteComment(moimId, boardId, commentId, loginUser);
            responseDTO.setItem("Comment successfully deleted.");
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok(responseDTO);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @GetMapping("/{moimId}/{boardId}/list-co")
    public ResponseEntity<?> getCommentList(@PathVariable("moimId") int moimId,
                                            @PathVariable("boardId") int boardId,
                                            @PageableDefault(size = 20) Pageable pageable,
                                            @RequestHeader("Authorization") String token) {

        ResponseDTO<CommentDTO> responseDTO = new ResponseDTO<>();

        String loggedInUsername = jwtTokenProvider.validateAndGetUsername(token);
        User loginUser = userRepository.findByUserId(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            Page<Comment> commentPage = commentService.getComment(moimId, boardId, pageable, loginUser);
            Page<CommentDTO> commentDtoPage = commentPage.map(Comment::EntityToDTO);

            responseDTO.setPageItems(commentDtoPage);
            responseDTO.setLastPage(commentDtoPage.isLast());

            ResponseDTO.PaginationInfo paginationInfo = new ResponseDTO.PaginationInfo();
            paginationInfo.setTotalPages(commentDtoPage.getTotalPages());
            paginationInfo.setCurrentPage(commentDtoPage.getNumber());
            paginationInfo.setTotalElements(commentDtoPage.getTotalElements());

            responseDTO.setPaginationInfo(paginationInfo);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok(responseDTO);

        } catch (AccessDeniedException e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
        }
    }


    @GetMapping("/{moimId}/list-my-co")
    public ResponseEntity<?> getMyComment(@PathVariable("moimId") int moimId,
                                          @PageableDefault(size = 20) Pageable pageable,
                                          @RequestHeader("Authorization") String token) {

        ResponseDTO<CommentDTO> responseDTO = new ResponseDTO<>();

        String loggedInUsername = jwtTokenProvider.validateAndGetUsername(token);
        User loginUser = userRepository.findByUserId(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            Page<Comment> commentPage = commentService.getMyComment(loginUser, moimId, pageable);
            Page<CommentDTO> commentDtoPage = commentPage.map(Comment::EntityToDTO);

            responseDTO.setPageItems(commentDtoPage);
            responseDTO.setLastPage(commentDtoPage.isLast());

            ResponseDTO.PaginationInfo paginationInfo = new ResponseDTO.PaginationInfo();
            paginationInfo.setTotalPages(commentDtoPage.getTotalPages());
            paginationInfo.setCurrentPage(commentDtoPage.getNumber());
            paginationInfo.setTotalElements(commentDtoPage.getTotalElements());

            responseDTO.setPaginationInfo(paginationInfo);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok(responseDTO);

        } catch (AccessDeniedException e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
        }


    }

}












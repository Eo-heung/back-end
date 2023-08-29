package com.example.backend.controller;

import com.example.backend.dto.BoardAndPictureDTO;
import com.example.backend.dto.BoardDTO;
import com.example.backend.dto.MoimDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.entity.*;
import com.example.backend.jwt.JwtTokenProvider;
import com.example.backend.repository.*;
import com.example.backend.service.BoardPictureService;
import com.example.backend.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    private final BoardPictureService boardPictureService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MoimRepository moimRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final BoardPictureRepository boardPictureRepository;
    private final MoimRegistrationRepository moimRegistrationRepository;

    @PostMapping(value = "/{moimId}/create-board", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Board> createBoard(@PathVariable("moimId") int moimId,
                                             @RequestHeader("Authorization") String token,
                                             @RequestParam("boardTitle") String boardTitle,
                                             @RequestParam("boardContent") String boardContent,
                                             @RequestParam("boardType") Board.BoardType boardType,
                                             @RequestParam(required = false, value="file") MultipartFile[] files
    ) {
        String loginUser = jwtTokenProvider.validateAndGetUsername(token);
        User checkUser = userRepository.findByUserId(loginUser)
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("boardTitle");
        System.out.println(boardTitle);
        BoardDTO boardDTO = BoardDTO.builder()
                .moimId(moimId)
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .userId(loginUser)
                .boardRegdate(LocalDateTime.now())
                .boardUpdate(LocalDateTime.now())
                .boardType(boardType)
                .build();

        List<BoardPicture> boardPictures = new ArrayList<>();

        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    byte[] picBytes;
                    try {
                        picBytes = file.getBytes();
                    } catch (IOException e) {
                        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
                    }

                    BoardPicture boardPicture = BoardPicture.builder()
                            .userId(checkUser)
                            .moimId(Moim.builder()
                                    .moimId(moimId)
                                    .build())
                            .boardPic(picBytes)
                            .createBoardPic(LocalDateTime.now())
                            .build();

                    boardPictures.add(boardPicture);
                }
            }
        }

        try {
            Board savedBoard = boardService.createBoard(checkUser, boardDTO.DTOToEntity(), boardPictures, moimId);
            return new ResponseEntity<>(savedBoard, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }



    @PostMapping("/{moimId}/free-board")
    public ResponseEntity<?> getFreeList(@PathVariable("moimId") int moimId,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(required = false, defaultValue = "") String keyword,
                                         @RequestParam(required = false, defaultValue = "all") String searchType,
                                         @RequestParam(defaultValue = "ascending") String orderBy,
                                         @RequestHeader("Authorization") String token,
                                         Pageable pageable) {
        String loggedInUsername = jwtTokenProvider.validateAndGetUsername(token);
        User loginUser = userRepository.findByUserId(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        pageable = PageRequest.of(0, (page + 1) * 10);

        try {
            Page<BoardDTO> freeBoards = boardService.getFreeBoard(loginUser, pageable, moimId, keyword, searchType, orderBy);
            return new ResponseEntity<>(freeBoards, HttpStatus.OK);
        } catch (NoSuchElementException | IllegalStateException e) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/{moimId}/notice-board")
    public ResponseEntity<?> getNoticeList(@PathVariable("moimId") int moimId,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(required = false, defaultValue = "") String keyword,
                                           @RequestParam(required = false, defaultValue = "all") String searchType,
                                           @RequestParam(defaultValue = "ascending") String orderBy,
                                           @RequestHeader("Authorization") String token,
                                           Pageable pageable) {
        String loggedInUsername = jwtTokenProvider.validateAndGetUsername(token);
        User loginUser = userRepository.findByUserId(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        pageable = PageRequest.of(0, (page + 1) * 10);

        try {
            Page<BoardDTO> noticeBoards = boardService.getNoticeBoard(loginUser, pageable, moimId, keyword, searchType, orderBy);
            return new ResponseEntity<>(noticeBoards, HttpStatus.OK);
        } catch (NoSuchElementException | IllegalStateException e) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("{moimId}/view-board/{boardId}")
    public ResponseEntity<?> viewBoard(@PathVariable int moimId,
                                      @PathVariable int boardId,
                                      @RequestHeader("Authorization") String token) {
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();
        String loggedInUsername = jwtTokenProvider.validateAndGetUsername(token);
        User loginUser = userRepository.findByUserId(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            Board boardEntity = boardService.viewboard(loginUser, boardId, moimId);


            List<BoardPicture> boardPictures = boardPictureService.viewBoardPic(
                    Board.builder()
                            .boardId(boardId)
                            .build()
            );

            BoardDTO returnBoardDTO = convertToDTO(boardEntity);


            List<String> base64EncodedPics = boardPictures.stream()
                    .map(pic -> Base64.getEncoder().encodeToString(pic.getBoardPic()))
                    .collect(Collectors.toList());

            Map<String, Object> returnMap = new HashMap<>();

            returnMap.put("boardDTO", returnBoardDTO);
            returnMap.put("boardPics", base64EncodedPics);

            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }



    @PostMapping("/{moimId}/delete-board/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable int moimId,
                                         @PathVariable int boardId,
                                         @RequestHeader("Authorization") String token) {
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();

        String loggedInUsername = jwtTokenProvider.validateAndGetUsername(token);
        User loginUser = userRepository.findByUserId(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));
        try {
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new Exception("게시글을 찾을 수 없습니다"));

            if (!board.getUserId().equals(loginUser)) {
                throw new IllegalStateException("You are not the author of this post.");
            }

            List<BoardPicture> boardPictures = boardPictureRepository.findByBoard(board);

            for (BoardPicture boardPicture : boardPictures) {
                boardPictureRepository.delete(boardPicture);
            }

            boardRepository.delete(board);

            Map<String, String> returnMap = new HashMap<>();
            returnMap.put("msg", "정상적으로 삭제되었습니다.");
            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }


    @PostMapping(value = "/{moimId}/modify-board/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> modifyBoard(
            @PathVariable int boardId,
            @PathVariable int moimId,
            @RequestBody BoardDTO boardDTO,
            @RequestParam(required = false) List<MultipartFile> newPictures,
            @RequestParam(required = false) List<Integer> deletePictureIds,
            @RequestParam(required = false) Map<Integer, MultipartFile> updatePicturesMap,
            @RequestHeader("Authorization") String token) {

        try {
            String loggedInUsername = jwtTokenProvider.validateAndGetUsername(token);
            User loginUser = userRepository.findByUserId(loggedInUsername)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            boardDTO.setBoardId(boardId);

            Board updatedBoard = boardService.modifyBoard(loginUser, boardDTO, newPictures, deletePictureIds, updatePicturesMap, moimId);

            ResponseDTO<Board> responseDTO = new ResponseDTO<>();
            responseDTO.setItem(updatedBoard);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            ResponseDTO<String> responseDTO = new ResponseDTO<>();
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }


    @PostMapping(value = "/{moimId}/verify-role")
    public ResponseEntity<?> verifyLeader(
            @PathVariable int moimId,
            @RequestHeader("Authorization") String token) {

        try {
            String loggedInUsername = jwtTokenProvider.validateAndGetUsername(token);
            User loginUser = userRepository.findByUserId(loggedInUsername)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Moim checkMoim = moimRepository.findById(moimId)
                    .orElseThrow(() -> new NoSuchElementException("Moim not found"));

            boolean isMember = boardService.verifyMemberRole(loginUser, checkMoim);
            boolean isLeader = boardService.verifyLeaderRole(loginUser, checkMoim);

            Map<String, Boolean> responseMap = new HashMap<>();
            responseMap.put("isMember", isMember);
            responseMap.put("isLeader", isLeader);

            ResponseDTO<Map<String, Boolean>> responseDTO = new ResponseDTO<>();
            responseDTO.setItem(responseMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            ResponseDTO<String> responseDTO = new ResponseDTO<>();
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }



    private BoardDTO convertToDTO(Board board) {
        return BoardDTO.builder()
                .boardId(board.getBoardId())
                .boardType(board.getBoardType())
                .userId(board.getUserId().getUserId())
                .userName(board.getUserId().getUserName())
                .moimId(board.getMoimId().getMoimId())
                .boardTitle(board.getBoardTitle())
                .boardContent(board.getBoardContent())
                .boardRegdate(board.getBoardRegdate())
                .build();
    }



}













package com.example.backend.dto;

import com.example.backend.entity.Board;
import com.example.backend.entity.BoardPicture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardAndPictureDTO {
    private Board board;
    private BoardPicture boardPicture;
}

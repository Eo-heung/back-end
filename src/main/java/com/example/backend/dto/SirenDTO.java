package com.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SirenDTO {

    private LocalDateTime singoDate;
    private String singozaId;
    private String pisingozaId;
    private String singoCategoryCode;
    private String singoContent;
    private String singoMsg;
    private byte[] singoImg1;
    private byte[] singoImg2;
    private byte[] singoImg3;


}
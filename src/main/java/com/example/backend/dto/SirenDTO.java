package com.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SirenDTO {

    private String singoDate;
    private String singozaId;
    private String pisingozaId;
    private String singoCategoryCode;
    private String singoContent;
    private List<Message> singoMsg;
    private byte[] singoImg1;
    private byte[] singoImg2;
    private byte[] singoImg3;


}


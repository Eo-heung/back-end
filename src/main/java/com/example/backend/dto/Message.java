package com.example.backend.dto;

import lombok.Data;

@Data
public class Message {
    private String content;
    private String type;
    private String timestamp;
}

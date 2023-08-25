package com.example.backend.dto;


import com.example.backend.entity.User;
import lombok.Data;

@Data
public class NaverDTO {
    private String state;
    private String client_id;
    private String client_secret;
    private String code;
    private String token;

    public User DTOToEntity() {
        return null;
    }
}

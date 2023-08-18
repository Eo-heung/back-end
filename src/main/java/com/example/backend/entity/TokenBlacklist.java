package com.example.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Optional;

@Entity
public class TokenBlacklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String token;

    // Getter and Setter 메서드
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}

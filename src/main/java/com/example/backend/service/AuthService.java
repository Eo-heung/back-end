package com.example.backend.service;

import com.example.backend.entity.TokenBlacklist;
import com.example.backend.repository.TokenBlacklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; // Import 추가

@Service
public class AuthService {
    @Autowired
    private TokenBlacklistRepository tokenBlacklistRepository;

    public void invalidateToken(String token) {
        TokenBlacklist blacklistedToken = new TokenBlacklist();
        blacklistedToken.setToken(token);
        tokenBlacklistRepository.save(blacklistedToken);
    }
}

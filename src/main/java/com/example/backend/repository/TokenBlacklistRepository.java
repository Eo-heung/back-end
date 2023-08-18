package com.example.backend.repository;

import com.example.backend.entity.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {
    Optional<TokenBlacklist> findByToken(String token);

}

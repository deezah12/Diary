package com.semicolon.diary.repositories;

import com.semicolon.diary.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    void deleteTokenByExpiredAtBefore(LocalDateTime now);
    @Transactional
    void confirmedAt(LocalDateTime now, String token);
}

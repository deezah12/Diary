package com.semicolon.diary.repositories;

import com.semicolon.diary.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.Optional;
@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    void deleteTokenByExpiredAtBefore(LocalDateTime now);
    @Transactional
    @Modifying
    @Query("UPDATE Token token " +
            "SET token.confirmedAt = ?1 " +
            "WHERE token.token = ?2 "
    )
    void setConfirmedAt(LocalDateTime now, String token);
}

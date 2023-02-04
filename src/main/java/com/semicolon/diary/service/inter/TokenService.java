package com.semicolon.diary.service.inter;

import com.semicolon.diary.entity.Token;

import java.util.Optional;

public interface TokenService {
    void  saveConfirmationToken(Token token);
    Optional<Token> getConfirmationToken(String token);
    void deleteExpiredToken();
    void setTokenConfirmationAt(String token);
}

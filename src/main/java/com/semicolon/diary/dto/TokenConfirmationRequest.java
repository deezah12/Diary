package com.semicolon.diary.dto;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class TokenConfirmationRequest {
    @NotNull
    private String token;
    @NotNull
    private String emailAddress;
}

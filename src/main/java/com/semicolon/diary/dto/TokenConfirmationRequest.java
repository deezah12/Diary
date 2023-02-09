package com.semicolon.diary.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class TokenConfirmationRequest {
    @NotNull
    private String token;
    @NotNull
    @Email
    private String emailAddress;
}

package com.semicolon.diary.service.inter;

import com.semicolon.diary.dto.*;
import com.semicolon.diary.entity.User;
import jakarta.mail.MessagingException;

public interface RegistrationService {
    SignUpResponse register(SignUpRequest signUpRequest) throws MessagingException;
    String tokenConfirmation(TokenConfirmationRequest tokenConfirmationRequest);
    String resendToken(ResendTokenRequest resendTokenRequest) throws MessagingException;
    String login(LoginRequest loginRequest);

}

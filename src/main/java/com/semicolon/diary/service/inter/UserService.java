package com.semicolon.diary.service.inter;

import com.semicolon.diary.dto.ChangePasswordRequest;
import com.semicolon.diary.dto.ForgotPasswordRequest;
import com.semicolon.diary.dto.ResetPasswordRequest;
import com.semicolon.diary.entity.User;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.Optional;

public interface UserService {
    String changePassword(ChangePasswordRequest changePasswordRequest);
    void getUser(User user);
    Optional<User> getByEmailAddress(String emailAddress);
    String forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws MessagingException;
    String resetPassword(ResetPasswordRequest resetPasswordRequest);
    void enableUser(String email);
    String generateToken (User user);
}

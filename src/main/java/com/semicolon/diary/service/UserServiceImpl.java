package com.semicolon.diary.service;

import com.semicolon.diary.dto.ChangePasswordRequest;
import com.semicolon.diary.dto.ForgotPasswordRequest;
import com.semicolon.diary.dto.ResetPasswordRequest;
import com.semicolon.diary.email.EmailSender;
import com.semicolon.diary.email.EmailService;
import com.semicolon.diary.entity.Token;
import com.semicolon.diary.entity.User;
import com.semicolon.diary.exceptions.GenericException;
import com.semicolon.diary.repositories.UserRepository;
import com.semicolon.diary.service.inter.TokenService;
import com.semicolon.diary.service.inter.UserService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    TokenService tokenService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailSender emailSender;
    @Override
    public void getUser(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> getByEmailAddress(String emailAddress) {
        return userRepository.findByEmailAddressIgnoreCase(emailAddress);

    }

    @Override
    public String forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws MessagingException {
        var foundUser = userRepository.findByEmailAddressIgnoreCase(forgotPasswordRequest
                .getEmailAddress());
        if (Objects.isNull(foundUser)) throw new GenericException("user does not exist");
        String token = generateToken(foundUser.get());
        emailSender.send(foundUser.get().getEmailAddress(), buildForgotPasswordEmail(foundUser.get().getLastName(), token));
        return token;
    }

    @Override
    public String resetPassword(ResetPasswordRequest resetPasswordRequest) {
        var tokenChecked = tokenService.getConfirmationToken(resetPasswordRequest.getToken())
                .orElseThrow(() -> new GenericException("Token does not exist"));

        if (tokenChecked.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new GenericException("Token has expired");
        }
        if (tokenChecked.getConfirmedAt() != null) {
            throw new GenericException("Token has been used");
        }
        tokenService.getConfirmationToken(tokenChecked.getToken());
        var user = userRepository.findByEmailAddressIgnoreCase(resetPasswordRequest.getEmailAddress());
        user.get().setPassword(hashPassword(resetPasswordRequest.getPassword()));
        userRepository.save(user.get());
        return "Your password successfully updated.";
    }

    private String hashPassword(String password) {
       return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public String changePassword (ChangePasswordRequest changePasswordRequest){
        var user = userRepository.findByEmailAddressIgnoreCase(changePasswordRequest.getEmailAddress())
                .orElseThrow(() -> new GenericException("invalid details"));

        if (!BCrypt.checkpw(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new GenericException("invalid details");
        }
        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword()))
            throw new GenericException("password do not match");
        user.setPassword(hashPassword(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
        return "password updated";
    }
    @Override
    public void enableUser(String email) {
        var foundEmail = userRepository.findByEmailAddressIgnoreCase(email).orElseThrow(() -> new GenericException("invalid email"));
        foundEmail.setVerified(true);
        userRepository.save(foundEmail);
    }

    @Override
    public String generateToken(User user) {
            SecureRandom random = new SecureRandom();
            String token = String.valueOf(1000 + random.nextInt(9999));
            Token confirmationToken = new Token(token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(10),
                    user
            );
            tokenService.saveConfirmationToken(confirmationToken);
            return confirmationToken.getToken();
    }

    private String buildForgotPasswordEmail (String lastName, String token){
        return "Here's the link to reset your password"
                + "                                      "
                + "                                        "
                + "<p>Hello \"" + lastName + "\",</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p>Token \"" + token + "\" Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";
    }
}

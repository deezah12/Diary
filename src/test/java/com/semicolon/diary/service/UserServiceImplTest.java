package com.semicolon.diary.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.semicolon.diary.dto.ChangePasswordRequest;
import com.semicolon.diary.dto.ForgotPasswordRequest;
import com.semicolon.diary.dto.ResetPasswordRequest;
import com.semicolon.diary.email.EmailSender;
import com.semicolon.diary.entity.Token;
import com.semicolon.diary.entity.User;
import com.semicolon.diary.exceptions.GenericException;
import com.semicolon.diary.repositories.UserRepository;
import com.semicolon.diary.service.inter.TokenService;
import jakarta.mail.MessagingException;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserServiceImpl.class})
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {
    @MockBean
    private EmailSender emailSender;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    /**
     * Method under test: {@link UserServiceImpl#getUser(User)}
     */
    @Test
    void testGetUser() {
        User user = new User("Jane", "Doe", "42 Main St", "iloveyou");

        when(userRepository.save((User) any())).thenReturn(user);
        assertSame(user, userServiceImpl.getUser(new User("Jane", "Doe", "42 Main St", "iloveyou")));
        verify(userRepository).save((User) any());
    }

    /**
     * Method under test: {@link UserServiceImpl#getUser(User)}
     */
    @Test
    void testGetUser2() {
        when(userRepository.save((User) any())).thenThrow(new GenericException("An error occurred"));
        assertThrows(GenericException.class,
                () -> userServiceImpl.getUser(new User("Jane", "Doe", "42 Main St", "iloveyou")));
        verify(userRepository).save((User) any());
    }

    /**
     * Method under test: {@link UserServiceImpl#getByEmailAddress(String)}
     */
    @Test
    void testGetByEmailAddress() {
        User user = new User("Jane", "Doe", "42 Main St", "iloveyou");

        when(userRepository.findByEmailAddressIgnoreCase((String) any())).thenReturn(user);
        assertSame(user, userServiceImpl.getByEmailAddress("42 Main St"));
        verify(userRepository).findByEmailAddressIgnoreCase((String) any());
    }

    /**
     * Method under test: {@link UserServiceImpl#getByEmailAddress(String)}
     */
    @Test
    void testGetByEmailAddress2() {
        when(userRepository.findByEmailAddressIgnoreCase((String) any()))
                .thenThrow(new GenericException("An error occurred"));
        assertThrows(GenericException.class, () -> userServiceImpl.getByEmailAddress("42 Main St"));
        verify(userRepository).findByEmailAddressIgnoreCase((String) any());
    }

    /**
     * Method under test: {@link UserServiceImpl#forgotPassword(ForgotPasswordRequest)}
     */
    @Test
    void testForgotPassword() throws MessagingException {
        doNothing().when(emailSender).send((String) any(), (String) any());
        doNothing().when(tokenService).saveConfirmationToken((Token) any());
        when(userRepository.findByEmailAddressIgnoreCase((String) any()))
                .thenReturn(new User("Jane", "Doe", "42 Main St", "iloveyou"));

        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setEmailAddress("42 Main St");
        userServiceImpl.forgotPassword(forgotPasswordRequest);
        verify(emailSender).send((String) any(), (String) any());
        verify(tokenService).saveConfirmationToken((Token) any());
        verify(userRepository).findByEmailAddressIgnoreCase((String) any());
    }

    /**
     * Method under test: {@link UserServiceImpl#forgotPassword(ForgotPasswordRequest)}
     */
    @Test
    void testForgotPassword2() throws MessagingException {
        doNothing().when(emailSender).send((String) any(), (String) any());
        doThrow(new GenericException("An error occurred")).when(tokenService).saveConfirmationToken((Token) any());
        when(userRepository.findByEmailAddressIgnoreCase((String) any()))
                .thenReturn(new User("Jane", "Doe", "42 Main St", "iloveyou"));

        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setEmailAddress("42 Main St");
        assertThrows(GenericException.class, () -> userServiceImpl.forgotPassword(forgotPasswordRequest));
        verify(tokenService).saveConfirmationToken((Token) any());
        verify(userRepository).findByEmailAddressIgnoreCase((String) any());
    }

    /**
     * Method under test: {@link UserServiceImpl#forgotPassword(ForgotPasswordRequest)}
     */
    @Test
    void testForgotPassword3() throws MessagingException {
        doNothing().when(emailSender).send((String) any(), (String) any());
        doNothing().when(tokenService).saveConfirmationToken((Token) any());
        when(userRepository.findByEmailAddressIgnoreCase((String) any())).thenReturn(null);

        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setEmailAddress("42 Main St");
        assertThrows(GenericException.class, () -> userServiceImpl.forgotPassword(forgotPasswordRequest));
        verify(userRepository).findByEmailAddressIgnoreCase((String) any());
    }

    /**
     * Method under test: {@link UserServiceImpl#forgotPassword(ForgotPasswordRequest)}
     */
    @Test
    void testForgotPassword4() throws MessagingException {
        doNothing().when(emailSender).send((String) any(), (String) any());
        doNothing().when(tokenService).saveConfirmationToken((Token) any());
        User user = mock(User.class);
        when(user.getEmailAddress()).thenThrow(new GenericException("An error occurred"));
        when(user.getLastName()).thenThrow(new GenericException("An error occurred"));
        when(userRepository.findByEmailAddressIgnoreCase((String) any())).thenReturn(user);

        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setEmailAddress("42 Main St");
        assertThrows(GenericException.class, () -> userServiceImpl.forgotPassword(forgotPasswordRequest));
        verify(tokenService).saveConfirmationToken((Token) any());
        verify(userRepository).findByEmailAddressIgnoreCase((String) any());
        verify(user).getEmailAddress();
    }

    /**
     * Method under test: {@link UserServiceImpl#resetPassword(ResetPasswordRequest)}
     */
    @Test
    void testResetPassword() {
        LocalDateTime createdAt = LocalDateTime.of(1, 1, 1, 1, 1);
        LocalDateTime expiredAt = LocalDateTime.of(1, 1, 1, 1, 1);
        when(tokenService.getConfirmationToken((String) any()))
                .thenReturn(new Token("ABC123", createdAt, expiredAt, new User("Jane", "Doe", "42 Main St", "iloveyou")));

        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setEmailAddress("42 Main St");
        resetPasswordRequest.setPassword("iloveyou");
        resetPasswordRequest.setToken("ABC123");
        assertThrows(GenericException.class, () -> userServiceImpl.resetPassword(resetPasswordRequest));
        verify(tokenService).getConfirmationToken((String) any());
    }

    /**
     * Method under test: {@link UserServiceImpl#resetPassword(ResetPasswordRequest)}
     */
    @Test
    void testResetPassword2() {
        when(tokenService.getConfirmationToken((String) any())).thenReturn(null);

        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setEmailAddress("42 Main St");
        resetPasswordRequest.setPassword("iloveyou");
        resetPasswordRequest.setToken("ABC123");
        assertThrows(GenericException.class, () -> userServiceImpl.resetPassword(resetPasswordRequest));
        verify(tokenService).getConfirmationToken((String) any());
    }

    /**
     * Method under test: {@link UserServiceImpl#resetPassword(ResetPasswordRequest)}
     */
    @Test
    void testResetPassword4() {
        Token token = mock(Token.class);
        when(token.getExpiredAt()).thenThrow(new GenericException("An error occurred"));
        when(tokenService.getConfirmationToken((String) any())).thenReturn(token);

        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setEmailAddress("42 Main St");
        resetPasswordRequest.setPassword("iloveyou");
        resetPasswordRequest.setToken("ABC123");
        assertThrows(GenericException.class, () -> userServiceImpl.resetPassword(resetPasswordRequest));
        verify(tokenService).getConfirmationToken((String) any());
        verify(token).getExpiredAt();
    }



    /**
     * Method under test: {@link UserServiceImpl#changePassword(ChangePasswordRequest)}
     */
    @Test
    void testChangePassword() {
        when(userRepository.findByEmailAddressIgnoreCase((String) any())).thenReturn(null);

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setConfirmPassword("iloveyou");
        changePasswordRequest.setEmailAddress("42 Main St");
        changePasswordRequest.setNewPassword("iloveyou");
        changePasswordRequest.setOldPassword("iloveyou");
        assertThrows(GenericException.class, () -> userServiceImpl.changePassword(changePasswordRequest));
        verify(userRepository).findByEmailAddressIgnoreCase((String) any());
    }

    /**
     * Method under test: {@link UserServiceImpl#changePassword(ChangePasswordRequest)}
     */
    @Test
    void testChangePassword2() {
        User user = mock(User.class);
        when(user.getPassword()).thenThrow(new GenericException("An error occurred"));
        when(userRepository.findByEmailAddressIgnoreCase((String) any())).thenReturn(user);

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setConfirmPassword("iloveyou");
        changePasswordRequest.setEmailAddress("42 Main St");
        changePasswordRequest.setNewPassword("iloveyou");
        changePasswordRequest.setOldPassword("iloveyou");
        assertThrows(GenericException.class, () -> userServiceImpl.changePassword(changePasswordRequest));
        verify(userRepository).findByEmailAddressIgnoreCase((String) any());
        verify(user).getPassword();
    }

    /**
     * Method under test: {@link UserServiceImpl#enableUser(String)}
     */
    @Test
    void testEnableUser() {
        when(userRepository.save((User) any())).thenReturn(new User("Jane", "Doe", "42 Main St", "iloveyou"));
        when(userRepository.findByEmailAddressIgnoreCase((String) any()))
                .thenReturn(new User("Jane", "Doe", "42 Main St", "iloveyou"));
        userServiceImpl.enableUser("jane.doe@example.org");
        verify(userRepository).findByEmailAddressIgnoreCase((String) any());
        verify(userRepository).save((User) any());
    }

    /**
     * Method under test: {@link UserServiceImpl#enableUser(String)}
     */
    @Test
    void testEnableUser2() {
        when(userRepository.save((User) any())).thenThrow(new GenericException("An error occurred"));
        when(userRepository.findByEmailAddressIgnoreCase((String) any()))
                .thenReturn(new User("Jane", "Doe", "42 Main St", "iloveyou"));
        assertThrows(GenericException.class, () -> userServiceImpl.enableUser("jane.doe@example.org"));
        verify(userRepository).findByEmailAddressIgnoreCase((String) any());
        verify(userRepository).save((User) any());
    }

    /**
     * Method under test: {@link UserServiceImpl#enableUser(String)}
     */
    @Test
    void testEnableUser3() {
        when(userRepository.save((User) any())).thenReturn(new User("Jane", "Doe", "42 Main St", "iloveyou"));
        when(userRepository.findByEmailAddressIgnoreCase((String) any())).thenReturn(null);
        assertThrows(GenericException.class, () -> userServiceImpl.enableUser("jane.doe@example.org"));
        verify(userRepository).findByEmailAddressIgnoreCase((String) any());
    }

    /**
     * Method under test: {@link UserServiceImpl#enableUser(String)}
     */
    @Test
    void testEnableUser4() {
        User user = mock(User.class);
        doThrow(new GenericException("An error occurred")).when(user).setVerified(anyBoolean());
        when(userRepository.save((User) any())).thenReturn(new User("Jane", "Doe", "42 Main St", "iloveyou"));
        when(userRepository.findByEmailAddressIgnoreCase((String) any())).thenReturn(user);
        assertThrows(GenericException.class, () -> userServiceImpl.enableUser("jane.doe@example.org"));
        verify(userRepository).findByEmailAddressIgnoreCase((String) any());
        verify(user).setVerified(anyBoolean());
    }

    /**
     * Method under test: {@link UserServiceImpl#generateToken(User)}
     */
    @Test
    void testGenerateToken() {
        doNothing().when(tokenService).saveConfirmationToken((Token) any());
        userServiceImpl.generateToken(new User("Jane", "Doe", "42 Main St", "iloveyou"));
        verify(tokenService).saveConfirmationToken((Token) any());
    }

    /**
     * Method under test: {@link UserServiceImpl#generateToken(User)}
     */
    @Test
    void testGenerateToken2() {
        doThrow(new GenericException("An error occurred")).when(tokenService).saveConfirmationToken((Token) any());
        assertThrows(GenericException.class,
                () -> userServiceImpl.generateToken(new User("Jane", "Doe", "42 Main St", "iloveyou")));
        verify(tokenService).saveConfirmationToken((Token) any());
    }
}


package com.semicolon.diary.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.semicolon.diary.dto.LoginRequest;
import com.semicolon.diary.dto.ResendTokenRequest;
import com.semicolon.diary.dto.SignUpRequest;
import com.semicolon.diary.dto.TokenConfirmationRequest;
import com.semicolon.diary.email.EmailSender;
import com.semicolon.diary.entity.Token;
import com.semicolon.diary.entity.User;
import com.semicolon.diary.exceptions.GenericException;
import com.semicolon.diary.service.inter.TokenService;
import com.semicolon.diary.service.inter.UserService;
import jakarta.mail.MessagingException;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = {RegistrationServiceImpl.class})
@ExtendWith(SpringExtension.class)
class RegistrationServiceImplTest {
    @MockBean
    private EmailSender emailSender;

    @Autowired
    private RegistrationServiceImpl registrationServiceImpl;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserService userService;


    /**
     * Method under test: {@link RegistrationServiceImpl#register(SignUpRequest)}
     */
    @Test
    void testRegister() throws MessagingException {
        RegistrationServiceImpl registrationServiceImpl = new RegistrationServiceImpl(
                userService,
                emailSender,
                tokenService
        );

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmailAddress("dija@gmail.com");
        signUpRequest.setFirstName("Jane");
        signUpRequest.setLastName("Doe");
        signUpRequest.setPassword("Iloveyou123");
        registrationServiceImpl.register(signUpRequest);
    }

    /**
     * Method under test: {@link RegistrationServiceImpl#register(SignUpRequest)}
     */
    @Test
    void testRegister2() throws MessagingException {
        when(userService.getByEmailAddress((String) any())).thenReturn(new User("Jane", "Doe", "dija@gmail.com", "iloveyou12!"));
        when(userService.getUser((User) any())).thenReturn(new User("Jane", "Doe", "dija@gmail.com", "iloveyou12!"));
        when(userService.generateToken((User) any())).thenReturn("9123");

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmailAddress("dija@gmail.com");
        signUpRequest.setFirstName("Jane");
        signUpRequest.setLastName("Doe");
        signUpRequest.setPassword("Iloveyou12!");
        assertThrows(GenericException.class, () -> registrationServiceImpl.register(signUpRequest));
        verify(userService).getByEmailAddress((String) any());
    }

    /**
     * Method under test: {@link RegistrationServiceImpl#tokenConfirmation(TokenConfirmationRequest)}
     */
    @Test
    void testTokenConfirmation() {
        LocalDateTime createdAt = LocalDateTime.of(1, 1, 1, 1, 1);
        LocalDateTime expiredAt = LocalDateTime.of(1, 1, 1, 1, 1);
        when(tokenService.getConfirmationToken((String) any()))
                .thenReturn(new Token("6123", createdAt, expiredAt, new User("Jane", "Doe", "42 Main St", "iloveyou")));
        assertThrows(GenericException.class,
                () -> registrationServiceImpl.tokenConfirmation(new TokenConfirmationRequest("6123", "42 Main St")));
        verify(tokenService).getConfirmationToken((String) any());
    }


    /**
     * Method under test: {@link RegistrationServiceImpl#tokenConfirmation(TokenConfirmationRequest)}
     */
    @Test
    void testTokenConfirmation2() {
        when(tokenService.getConfirmationToken((String) any())).thenThrow(new GenericException("An error occurred"));
        assertThrows(GenericException.class,
                () -> registrationServiceImpl.tokenConfirmation(new TokenConfirmationRequest("0123", "dija@gmail.com")));
        verify(tokenService).getConfirmationToken((String) any());
    }

    /**
     * Method under test: {@link RegistrationServiceImpl#resendToken(ResendTokenRequest)}
     */
    @Test
    void testResendToken() throws MessagingException {
        doNothing().when(emailSender).send((String) any(), (String) any());
        when(userService.generateToken((User) any())).thenReturn("0123");
        when(userService.getByEmailAddress((String) any())).thenReturn(new User("Jane", "Doe", "dija@gmail.com", "Iloveyou2@"));

        ResendTokenRequest resendTokenRequest = new ResendTokenRequest();
        resendTokenRequest.setEmailAddress("dija@gmail.com");
        assertEquals("token has been resent successfully", registrationServiceImpl.resendToken(resendTokenRequest));
        verify(emailSender).send((String) any(), (String) any());
        verify(userService).getByEmailAddress((String) any());
        verify(userService).generateToken((User) any());
    }

    /**
     * Method under test: {@link RegistrationServiceImpl#resendToken(ResendTokenRequest)}
     */
    @Test
    void testResendToken2() throws MessagingException {
        doNothing().when(emailSender).send((String) any(), (String) any());
        when(userService.generateToken((User) any())).thenThrow(new GenericException("User already exist"));
        when(userService.getByEmailAddress((String) any())).thenReturn(new User("Jane", "Doe", "dija@gmail.com", "Iloveyou2@"));

        ResendTokenRequest resendTokenRequest = new ResendTokenRequest();
        resendTokenRequest.setEmailAddress("dija@gmail.com");
        assertThrows(GenericException.class, () -> registrationServiceImpl.resendToken(resendTokenRequest));
        verify(userService).getByEmailAddress((String) any());
        verify(userService).generateToken((User) any());
    }


    /**
     * Method under test: {@link RegistrationServiceImpl#resendToken(ResendTokenRequest)}
     */
    @Test
    void testResendToken3() throws MessagingException {
        doNothing().when(emailSender).send((String) any(), (String) any());
        User user = mock(User.class);
        when(user.isVerified()).thenReturn(true);
        when(user.getFirstName()).thenReturn("Jane");
        when(userService.generateToken((User) any())).thenReturn("0123");
        when(userService.getByEmailAddress((String) any())).thenReturn(user);

        ResendTokenRequest resendTokenRequest = new ResendTokenRequest();
        resendTokenRequest.setEmailAddress("dija@gmail.com");
        assertThrows(GenericException.class, () -> registrationServiceImpl.resendToken(resendTokenRequest));
        verify(userService).getByEmailAddress((String) any());
        verify(user).isVerified();
    }

    /**
     * Method under test: {@link RegistrationServiceImpl#login(LoginRequest)}
     */
    @Test
    void testLogin() {
        when(userService.getByEmailAddress((String) any())).thenReturn(new User("Jane", "Doe", "dija@gmail.com", "Iloveyou1@"));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmailAddress("dija@gmail.com");
        loginRequest.setPassword("Iloveyou1@");
        assertThrows(GenericException.class, () -> registrationServiceImpl.login(loginRequest));
        verify(userService).getByEmailAddress((String) any());
    }

    /**
     * Method under test: {@link RegistrationServiceImpl#login(LoginRequest)}
     */
    @Test
    void testLogin2() {
        when(userService.getByEmailAddress((String) any())).thenReturn(null);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmailAddress("dija@gmail.com");
        loginRequest.setPassword("Iloveyou1@");
        assertThrows(GenericException.class, () -> registrationServiceImpl.login(loginRequest));
        verify(userService).getByEmailAddress((String) any());
    }


    /**
     * Method under test: {@link RegistrationServiceImpl#login(LoginRequest)}
     */
    @Test
    void testLogin3() {
        User user = mock(User.class);
        when(user.getPassword()).thenThrow(new GenericException("Invalid details"));
        when(user.isVerified()).thenReturn(true);
        when(userService.getByEmailAddress((String) any())).thenReturn(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmailAddress("dija@gmail.com");
        loginRequest.setPassword("Iloveyou1@");
        assertThrows(RuntimeException.class, () -> registrationServiceImpl.login(loginRequest));
        verify(userService).getByEmailAddress((String) any());
        verify(user).isVerified();
        verify(user).getPassword();
    }

    /**
     * Method under test: {@link RegistrationServiceImpl#login(LoginRequest)}
     */
    @Test
    void testLogin4() {
        User user = mock(User.class);
        when(user.getPassword()).thenThrow(new RuntimeException());
        when(user.isVerified()).thenReturn(true);
        when(userService.getByEmailAddress((String) any())).thenReturn(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmailAddress("dija@gmail.com");
        loginRequest.setPassword("iloveyou1@D");
        assertThrows(RuntimeException.class, () -> registrationServiceImpl.login(loginRequest));
        verify(userService).getByEmailAddress((String) any());
        verify(user).isVerified();
        verify(user).getPassword();
    }

    /**
     * Method under test: {@link RegistrationServiceImpl#login(LoginRequest)}
     */
    @Test
    void testLogin5() {
        User user = mock(User.class);
        when(user.isVerified()).thenThrow(new GenericException("Not verified"));
        when(userService.getByEmailAddress((String) any())).thenReturn(user);
        LoginRequest loginRequest = mock(LoginRequest.class);
        when(loginRequest.getEmailAddress()).thenReturn("dija@gmail.com");
        when(loginRequest.getPassword()).thenReturn("iloveyou1@Q");
        doNothing().when(loginRequest).setEmailAddress((String) any());
        doNothing().when(loginRequest).setPassword((String) any());
        loginRequest.setEmailAddress("dija@gmail.com");
        loginRequest.setPassword("iloveyou1@Q");
        assertThrows(GenericException.class, () -> registrationServiceImpl.login(loginRequest));
        verify(userService).getByEmailAddress((String) any());
        verify(user).isVerified();
        verify(loginRequest).getEmailAddress();
        verify(loginRequest).setEmailAddress((String) any());
        verify(loginRequest).setPassword((String) any());
    }
}


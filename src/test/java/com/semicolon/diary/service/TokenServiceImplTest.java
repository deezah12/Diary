// package com.semicolon.diary.service;

// import static org.junit.jupiter.api.Assertions.assertSame;
// import static org.mockito.Mockito.any;
// import static org.mockito.Mockito.doNothing;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;

// import com.semicolon.diary.entity.Token;
// import com.semicolon.diary.repositories.TokenRepository;

// import java.time.LocalDateTime;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.test.annotation.DirtiesContext;
// import org.springframework.test.context.ContextConfiguration;
// import org.springframework.test.context.junit.jupiter.SpringExtension;

// @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
// @ContextConfiguration(classes = {TokenServiceImpl.class})
// @ExtendWith(SpringExtension.class)
// class TokenServiceImplTest {
//     @MockBean
//     private TokenRepository tokenRepository;

//     @Autowired
//     private TokenServiceImpl tokenServiceImpl;

//     /**
//      * Method under test: {@link TokenServiceImpl#saveConfirmationToken(Token)}
//      */
//     @Test
//     void testSaveConfirmationToken() {
//         when(tokenRepository.save((Token) any())).thenReturn(new Token());
//         tokenServiceImpl.saveConfirmationToken(new Token());
//         verify(tokenRepository).save((Token) any());
//     }

//     /**
//      * Method under test: {@link TokenServiceImpl#getConfirmationToken(String)}
//      */
//     @Test
//     void testGetConfirmationToken() {
//         Token token = new Token();
//         when(tokenRepository.findByToken((String) any())).thenReturn(token);
//         assertSame(token, tokenServiceImpl.getConfirmationToken("0123"));
//         verify(tokenRepository).findByToken((String) any());
//     }

//     /**
//      * Method under test: {@link TokenServiceImpl#deleteExpiredToken()}
//      */
//     @Test
//     void testDeleteExpiredToken() {
//         doNothing().when(tokenRepository).deleteTokenByExpiredAtBefore((LocalDateTime) any());
//         tokenServiceImpl.deleteExpiredToken();
//         verify(tokenRepository).deleteTokenByExpiredAtBefore((LocalDateTime) any());
//     }

//     /**
//      * Method under test: {@link TokenServiceImpl#setTokenConfirmationAt(String)}
//      */
//     @Test
//     void testSetTokenConfirmationAt() {
//         doNothing().when(tokenRepository).setConfirmedAt((LocalDateTime) any(), (String) any());
//         tokenServiceImpl.setTokenConfirmationAt("0123");
//         verify(tokenRepository).setConfirmedAt((LocalDateTime) any(), (String) any());
//     }
// }


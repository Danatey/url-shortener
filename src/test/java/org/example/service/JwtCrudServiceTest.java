package org.example.service;

import org.example.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class JwtCrudServiceTest {

    private JwtCrudService jwtService;

    @BeforeEach
    void setUp() {
        String secret = "my-super-secret-key-my-super-secret-key";
        jwtService = new JwtCrudService(secret, 3600000);
    }

    @Test
    void shouldGenerateToken() {
        User user = User.builder()
                .username("testUser")
                .build();

        String token = jwtService.generateToken(user);

        Assertions.assertNotNull(token);
        Assertions.assertFalse(token.isEmpty());
    }

    @Test
    void shouldExtractUsernameFromToken() {
        User user = User.builder()
                .username("testUser")
                .build();

        String token = jwtService.generateToken(user);

        String username = jwtService.extractUsername(token);

        Assertions.assertEquals("testUser", username);
    }

    @Test
    void shouldValidateTokenSuccessfully() {
        User user = User.builder()
                .username("testUser")
                .build();

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("testUser")
                .password("pass")
                .authorities("USER")
                .build();

        String token = jwtService.generateToken(user);

        boolean result = jwtService.isTokenValid(token, userDetails);

        Assertions.assertTrue(result);
    }

    @Test
    void shouldReturnFalseForDifferentUser() {
        User user = User.builder()
                .username("testUser")
                .build();

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("otherUser")
                .password("pass")
                .authorities("USER")
                .build();

        String token = jwtService.generateToken(user);

        boolean result = jwtService.isTokenValid(token, userDetails);

        Assertions.assertFalse(result);
    }

    @Test
    void shouldFailOnTamperedToken() {
        User user = User.builder()
                .username("testUser")
                .build();

        String token = jwtService.generateToken(user);

        String tampered = token + "abc";

        Assertions.assertThrows(Exception.class, () -> {
            jwtService.extractUsername(tampered);
        });
    }

    @Test
    void shouldNotValidateTokenWithWrongUser() {
        User user = User.builder()
                .username("user1")
                .build();

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("user2")
                .password("pass")
                .authorities("USER")
                .build();

        String token = jwtService.generateToken(user);

        Assertions.assertFalse(jwtService.isTokenValid(token, userDetails));
    }
}

package org.example.service;

import org.example.dto.AuthResponseDto;
import org.example.dto.LoginRequestDto;
import org.example.dto.RegisterRequestDto;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthCrudServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthCrudService authCrudService;

    @Test
    void shouldRegisterUserSuccessfully() {
        RegisterRequestDto request = new RegisterRequestDto();
        request.setUsername("test");
        request.setPassword("pass");

        Mockito.when(userRepository.existsByUsername("test"))
                .thenReturn(false);

        Mockito.when(passwordEncoder.encode("pass"))
                .thenReturn("encoded-pass");

        authCrudService.register(request);

        Mockito.verify(userRepository).save(Mockito.argThat(user ->
                user.getUsername().equals("test") &&
                        user.getPassword().equals("encoded-pass")
        ));
    }

    @Test
    void shouldThrowExceptionWhenUsernameExists() {
        RegisterRequestDto request = new RegisterRequestDto();
        request.setUsername("test");
        request.setPassword("pass");

        Mockito.when(userRepository.existsByUsername("test"))
                .thenReturn(true);

        org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> authCrudService.register(request)
        );

        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void shouldLoginSuccessfully() {
        LoginRequestDto request = new LoginRequestDto();
        request.setUsername("test");
        request.setPassword("pass");

        User user = User.builder()
                .username("test")
                .password("encoded-pass")
                .build();

        Mockito.when(userRepository.findByUsername("test"))
                .thenReturn(java.util.Optional.of(user));

        Mockito.when(passwordEncoder.matches("pass", "encoded-pass"))
                .thenReturn(true);

        Mockito.when(jwtService.generateToken(user))
                .thenReturn("jwt-token");

        AuthResponseDto response = authCrudService.login(request);

        org.junit.jupiter.api.Assertions.assertEquals("jwt-token", response.getToken());
    }

    @Test
    void shouldThrowExceptionWhenUsernameNotFound() {
        LoginRequestDto request = new LoginRequestDto();
        request.setUsername("test");
        request.setPassword("pass");

        Mockito.when(userRepository.findByUsername("test"))
                .thenReturn(java.util.Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> authCrudService.login(request)
        );
    }

    @Test
    void shouldThrowExceptionWhenPasswordInvalid() {
        LoginRequestDto request = new LoginRequestDto();
        request.setUsername("test");
        request.setPassword("wrong");

        User user = User.builder()
                .username("test")
                .password("encoded-pass")
                .build();

        Mockito.when(userRepository.findByUsername("test"))
                .thenReturn(java.util.Optional.of(user));

        Mockito.when(passwordEncoder.matches("wrong", "encoded-pass"))
                .thenReturn(false);

        org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> authCrudService.login(request)
        );
    }
}

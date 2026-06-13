package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.config.JwtAuthFilter;
import org.example.dto.AuthResponseDto;
import org.example.dto.LoginRequestDto;
import org.example.dto.RegisterRequestDto;
import org.example.service.AuthCrudService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthCrudService authCrudService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRegisterUserReturnCreated() throws Exception {

        RegisterRequestDto request = new RegisterRequestDto();
        request.setUsername("testUser");
        request.setPassword("Password123");

        doNothing().when(authCrudService).register(any(RegisterRequestDto.class));

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldLoginUserReturnOkAndToken() throws Exception {

        LoginRequestDto request = new LoginRequestDto();
        request.setUsername("testUser");
        request.setPassword("Password123");

        AuthResponseDto response = AuthResponseDto.builder()
                .token("jwt-token")
                .build();

        when(authCrudService.login(any(LoginRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }

    @Test
    void shouldReturnBadRequestWhenRegisterThrowsException() throws Exception {

        RegisterRequestDto request = new RegisterRequestDto();
        request.setUsername("testUser");
        request.setPassword("Password123");

        doThrow(new RuntimeException("error"))
                .when(authCrudService)
                .register(any(RegisterRequestDto.class));

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
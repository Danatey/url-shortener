package org.example.controller;

import org.example.config.SecurityWhitelist;
import org.example.dto.UrlResponseDto;
import org.example.service.JwtService;
import org.example.service.UrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShortUrlController.class)
@AutoConfigureMockMvc(addFilters = false)
class ShortUrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlService urlService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private SecurityWhitelist securityWhitelist;

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void shouldCreateShortUrl() throws Exception {

        UrlResponseDto response = new UrlResponseDto(
                UUID.randomUUID(),
                "abc123",
                "https://google.com",
                "testUser",
                0L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(urlService.create(any(), anyString()))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/short-url")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "originalUrl": "https://google.com",
                          "expirationDays": 7
                        }
                        """))
                .andExpect(status().isOk());
    }
}
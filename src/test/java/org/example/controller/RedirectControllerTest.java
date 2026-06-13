package org.example.controller;

import org.example.config.SecurityWhitelist;
import org.example.service.JwtService;
import org.example.service.UrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RedirectController.class)
@AutoConfigureMockMvc(addFilters = false)
class RedirectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlService urlService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private SecurityWhitelist securityWhitelist;

    @Test
    void shouldReturnRedirectWhenShortCodeIsValid() throws Exception {

        String shortCode = "abc123";
        String originalUrl = "https://google.com";

        when(urlService.redirect(shortCode))
                .thenReturn(originalUrl);

        mockMvc.perform(get("/r/{shortCode}", shortCode))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", originalUrl));
    }

    @Test
    void shouldReturnFoundStatusAndLocationHeader() throws Exception {

        String shortCode = "xyz789";
        String originalUrl = "https://example.com";

        when(urlService.redirect(shortCode))
                .thenReturn(originalUrl);

        mockMvc.perform(get("/r/{shortCode}", shortCode))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", originalUrl));
    }
}
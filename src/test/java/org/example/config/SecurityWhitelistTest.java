package org.example.config;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityWhitelistTest {

    private final SecurityWhitelist whitelist = new SecurityWhitelist();

    @Test
    void shouldReturnTrueForAuthPath() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/v1/auth/login");

        boolean result = whitelist.isPublic(request);

        assertTrue(result);
    }

    @Test
    void shouldReturnTrueForSwaggerPath() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/swagger-ui/index.html");

        boolean result = whitelist.isPublic(request);

        assertTrue(result);
    }

    @Test
    void shouldReturnTrueForApiDocsPath() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/v3/api-docs");

        boolean result = whitelist.isPublic(request);

        assertTrue(result);
    }

    @Test
    void shouldReturnTrueForRedirectPath() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/r/abc123");

        boolean result = whitelist.isPublic(request);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseForProtectedPath() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/v1/short-url");

        boolean result = whitelist.isPublic(request);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseForUnknownPath() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/admin/panel");

        boolean result = whitelist.isPublic(request);

        assertFalse(result);
    }
}
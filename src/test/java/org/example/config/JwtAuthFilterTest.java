package org.example.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.mockito.Mockito.*;

class JwtAuthFilterTest {

    private JwtService jwtService;
    private UserDetailsService userDetailsService;
    private SecurityWhitelist whitelist;

    private JwtAuthFilter filter;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        jwtService = mock(JwtService.class);
        userDetailsService = mock(UserDetailsService.class);
        whitelist = mock(SecurityWhitelist.class);

        filter = new JwtAuthFilter(jwtService, userDetailsService, whitelist);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
    }

    @Test
    void shouldSkipFilterWhenPublic() throws Exception {
        when(whitelist.isPublic(request)).thenReturn(true);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
    }

    @Test
    void shouldSkipWhenNoAuthHeader() throws Exception {
        when(whitelist.isPublic(request)).thenReturn(false);
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
    }

    @Test
    void shouldSkipWhenInvalidHeader() throws Exception {
        when(whitelist.isPublic(request)).thenReturn(false);
        when(request.getHeader("Authorization")).thenReturn("InvalidToken");

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
    }

    @Test
    void shouldAuthenticateWhenTokenValid() throws Exception {
        when(whitelist.isPublic(request)).thenReturn(false);
        when(request.getHeader("Authorization")).thenReturn("Bearer token");

        when(jwtService.extractUsername("token")).thenReturn("user1");

        UserDetails userDetails = User
                .withUsername("user1")
                .password("pass")
                .authorities("USER")
                .build();

        when(userDetailsService.loadUserByUsername("user1"))
                .thenReturn(userDetails);

        when(jwtService.isTokenValid("token", userDetails))
                .thenReturn(true);

        filter.doFilterInternal(request, response, filterChain);

        verify(jwtService).extractUsername("token");
        verify(userDetailsService).loadUserByUsername("user1");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWhenTokenInvalid() throws Exception {
        when(whitelist.isPublic(request)).thenReturn(false);
        when(request.getHeader("Authorization")).thenReturn("Bearer token");

        when(jwtService.extractUsername("token")).thenReturn("user1");

        UserDetails userDetails = User
                .withUsername("user1")
                .password("pass")
                .authorities("USER")
                .build();

        when(userDetailsService.loadUserByUsername("user1"))
                .thenReturn(userDetails);

        when(jwtService.isTokenValid("token", userDetails))
                .thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldClearContextOnException() throws Exception {
        when(whitelist.isPublic(request)).thenReturn(false);
        when(request.getHeader("Authorization")).thenReturn("Bearer token");

        when(jwtService.extractUsername("token"))
                .thenThrow(new RuntimeException("error"));

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
}
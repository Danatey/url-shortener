package org.example.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.List;

@Component
public class SecurityWhitelist {

    private static final List<String> WHITELIST = List.of(
            "/api/v1/auth/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/r/**"
    );

    public boolean isPublic(HttpServletRequest request) {
        String path = request.getRequestURI();

        return WHITELIST.stream()
                .anyMatch(pattern -> new AntPathMatcher().match(pattern, path));
    }
}

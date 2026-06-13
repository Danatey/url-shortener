package org.example.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthResponseDtoTest {

    @Test
    void shouldBuildDtoWithBuilder() {
        AuthResponseDto dto = AuthResponseDto.builder()
                .token("jwt-token-123")
                .build();

        assertNotNull(dto);
        assertEquals("jwt-token-123", dto.getToken());
    }

    @Test
    void shouldUseNoArgsConstructorAndSetterGetter() {
        AuthResponseDto dto = new AuthResponseDto();

        dto.setToken("abc-token");

        assertEquals("abc-token", dto.getToken());
    }

    @Test
    void shouldUseAllArgsConstructor() {
        AuthResponseDto dto = new AuthResponseDto("direct-token");

        assertEquals("direct-token", dto.getToken());
    }
}
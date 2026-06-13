package org.example.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateShortUrlRequestDtoTest {

    @Test
    void shouldBuildDto() {
        CreateShortUrlRequestDto dto = CreateShortUrlRequestDto.builder()
                .originalUrl("https://google.com")
                .build();

        assertNotNull(dto);
        assertEquals("https://google.com", dto.getOriginalUrl());
    }

    @Test
    void shouldSetAndGetFields() {
        CreateShortUrlRequestDto dto = new CreateShortUrlRequestDto();
        dto.setOriginalUrl("https://example.com");

        assertEquals("https://example.com", dto.getOriginalUrl());
    }
}
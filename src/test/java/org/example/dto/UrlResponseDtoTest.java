package org.example.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UrlResponseDtoTest {

    @Test
    void shouldBuildWithBuilder() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        UrlResponseDto dto = UrlResponseDto.builder()
                .id(id)
                .originalUrl("https://example.com")
                .shortCode("abc123")
                .shortUrl("http://short/abc123")
                .clickCount(10L)
                .createdAt(now)
                .expiresAt(now.plusDays(1))
                .build();

        assertEquals(id, dto.getId());
        assertEquals("https://example.com", dto.getOriginalUrl());
        assertEquals("abc123", dto.getShortCode());
        assertEquals("http://short/abc123", dto.getShortUrl());
        assertEquals(10L, dto.getClickCount());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now.plusDays(1), dto.getExpiresAt());
    }

    @Test
    void shouldCoverNoArgsConstructorAndSetters() {
        UrlResponseDto dto = new UrlResponseDto();

        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        dto.setId(id);
        dto.setOriginalUrl("https://google.com");
        dto.setShortCode("xyz");
        dto.setShortUrl("http://short/xyz");
        dto.setClickCount(5L);
        dto.setCreatedAt(now);
        dto.setExpiresAt(now.plusDays(2));

        assertEquals(id, dto.getId());
        assertEquals("https://google.com", dto.getOriginalUrl());
        assertEquals("xyz", dto.getShortCode());
        assertEquals("http://short/xyz", dto.getShortUrl());
        assertEquals(5L, dto.getClickCount());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now.plusDays(2), dto.getExpiresAt());
    }
}
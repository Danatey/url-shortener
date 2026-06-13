package org.example.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UpdateShortUrlRequestDtoTest {

    @Test
    void shouldWorkWithGettersAndSetters() {
        UpdateShortUrlRequestDto dto = new UpdateShortUrlRequestDto();
        LocalDateTime now = LocalDateTime.now();

        dto.setOriginalUrl("https://example.com");
        dto.setExpiresAt(now);

        assertEquals("https://example.com", dto.getOriginalUrl());
        assertEquals(now, dto.getExpiresAt());
    }

    @Test
    void shouldBeEqualWhenFieldsAreSame() {
        LocalDateTime now = LocalDateTime.now();

        UpdateShortUrlRequestDto dto1 = new UpdateShortUrlRequestDto();
        dto1.setOriginalUrl("https://google.com");
        dto1.setExpiresAt(now);

        UpdateShortUrlRequestDto dto2 = new UpdateShortUrlRequestDto();
        dto2.setOriginalUrl("https://google.com");
        dto2.setExpiresAt(now);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferent() {
        UpdateShortUrlRequestDto dto1 = new UpdateShortUrlRequestDto();
        dto1.setOriginalUrl("https://google.com");

        UpdateShortUrlRequestDto dto2 = new UpdateShortUrlRequestDto();
        dto2.setOriginalUrl("https://yahoo.com");

        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldCallToStringWithoutErrors() {
        UpdateShortUrlRequestDto dto = new UpdateShortUrlRequestDto();
        dto.setOriginalUrl("https://example.com");

        String result = dto.toString();

        assertNotNull(result);
        assertTrue(result.contains("example.com"));
    }

    @Test
    void shouldHandleComparisonWithDifferentType() {
        UpdateShortUrlRequestDto dto = new UpdateShortUrlRequestDto();
        dto.setOriginalUrl("https://example.com");

        assertNotEquals(dto, "string"); // triggers canEqual
    }
}
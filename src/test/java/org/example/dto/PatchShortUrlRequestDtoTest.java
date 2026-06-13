package org.example.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PatchShortUrlRequestDtoTest {

    @Test
    void shouldWorkWithGettersAndSetters() {
        PatchShortUrlRequestDto dto = new PatchShortUrlRequestDto();
        LocalDateTime now = LocalDateTime.now();

        dto.setOriginalUrl("https://example.com");
        dto.setExpiresAt(now);

        assertEquals("https://example.com", dto.getOriginalUrl());
        assertEquals(now, dto.getExpiresAt());
    }

    @Test
    void shouldBeEqualWhenFieldsAreSame() {
        LocalDateTime now = LocalDateTime.now();

        PatchShortUrlRequestDto dto1 = new PatchShortUrlRequestDto();
        dto1.setOriginalUrl("https://google.com");
        dto1.setExpiresAt(now);

        PatchShortUrlRequestDto dto2 = new PatchShortUrlRequestDto();
        dto2.setOriginalUrl("https://google.com");
        dto2.setExpiresAt(now);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentValues() {
        PatchShortUrlRequestDto dto1 = new PatchShortUrlRequestDto();
        dto1.setOriginalUrl("https://google.com");

        PatchShortUrlRequestDto dto2 = new PatchShortUrlRequestDto();
        dto2.setOriginalUrl("https://yahoo.com");

        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldCallToString() {
        PatchShortUrlRequestDto dto = new PatchShortUrlRequestDto();
        dto.setOriginalUrl("https://example.com");

        String result = dto.toString();

        assertNotNull(result);
        assertTrue(result.contains("example.com"));
    }

    @Test
    void shouldHandleComparisonWithDifferentType() {
        PatchShortUrlRequestDto dto = new PatchShortUrlRequestDto();
        dto.setOriginalUrl("https://example.com");

        assertNotEquals(dto, "string");
    }
}
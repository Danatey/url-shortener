package org.example.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseDtoTest {

    @Test
    void shouldCreateErrorResponseDtoAndReturnCorrectValues() {
        String message = "Something went wrong";
        int status = 500;
        LocalDateTime timestamp = LocalDateTime.now();

        ErrorResponseDto dto = new ErrorResponseDto(message, status, timestamp);

        assertEquals(message, dto.message());
        assertEquals(status, dto.status());
        assertEquals(timestamp, dto.timestamp());
    }

    @Test
    void recordShouldSupportEqualsAndHashCode() {
        LocalDateTime timestamp = LocalDateTime.now();

        ErrorResponseDto dto1 = new ErrorResponseDto("Error", 400, timestamp);
        ErrorResponseDto dto2 = new ErrorResponseDto("Error", 400, timestamp);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }
}
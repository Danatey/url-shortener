package org.example.dto;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        String message,
        int status,
        LocalDateTime timestamp
) {}
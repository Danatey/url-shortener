package org.example.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PatchShortUrlRequestDto {

    private String originalUrl;
    private LocalDateTime expiresAt;
}
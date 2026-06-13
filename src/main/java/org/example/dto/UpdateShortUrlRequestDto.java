package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateShortUrlRequestDto {

    @NotBlank
    private String originalUrl;

    private LocalDateTime expiresAt;
}
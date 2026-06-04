package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateShortUrlRequestDto {
    @NotBlank
    private String originalUrl;

    private Integer expirationDays;
}

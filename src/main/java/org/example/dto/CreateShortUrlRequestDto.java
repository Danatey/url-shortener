package org.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateShortUrlRequestDto {
    @NotBlank(message = "URL cannot be empty")
    private String originalUrl;

    @Min(value = 1, message = "Expiration must be at least 1 day")
    private Integer expirationDays;
}

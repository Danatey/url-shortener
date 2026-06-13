package org.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateShortUrlRequestDto {
    @NotBlank(message = "URL cannot be empty")
    private String originalUrl;

    @NotNull
    @Min(value = 1, message = "Expiration must be at least 1 day")
    private Integer expirationDays;
}

package com.example.urlshortner.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlCreateRequestDTO {
    @NotBlank(message = "Url is required")
    private String longUrl;

    @Pattern(
            regexp = "^[a-zA-Z0-9_-]{3,20}$",
            message = "Alias must be 3-20 characters and contain only letters, numbers, - or _"
    )
    private String customAlias;

    private LocalDateTime expiryDate;
}

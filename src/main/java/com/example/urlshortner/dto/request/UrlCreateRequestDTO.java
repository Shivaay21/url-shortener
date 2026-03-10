package com.example.urlshortner.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UrlCreateRequestDTO {
    @NotBlank(message = "Url is required")
    private String longUrl;
    @NotBlank(message = "Alias is required")
    private String customAlias;
    @NotBlank(message = "Expiry date is required")
    private LocalDateTime expiryDate;
}

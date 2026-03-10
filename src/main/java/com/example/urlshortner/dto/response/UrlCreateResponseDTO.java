package com.example.urlshortner.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UrlCreateResponseDTO {
    private String shortUrl;
    private String shortCode;
    private String longUrl;
    private LocalDateTime expiryDate;
}

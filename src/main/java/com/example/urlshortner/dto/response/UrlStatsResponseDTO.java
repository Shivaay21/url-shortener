package com.example.urlshortner.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UrlStatsResponseDTO {
    private String longUrl;
    private String shortCode;
    private int clickCount;
    private LocalDateTime createdAt;
    private LocalDateTime expiryDate;
}

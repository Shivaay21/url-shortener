package com.example.urlshortner.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UrlAnalyticsResponseDTO {
    private String shortCode;
    private int totalClicks;
    private int clicksToday;
}

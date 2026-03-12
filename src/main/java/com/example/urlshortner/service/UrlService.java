package com.example.urlshortner.service;

import com.example.urlshortner.dto.request.UrlCreateRequestDTO;
import com.example.urlshortner.dto.response.UrlCreateResponseDTO;
import com.example.urlshortner.dto.response.UrlStatsResponseDTO;

public interface UrlService {
    UrlCreateResponseDTO createShortUrl(UrlCreateRequestDTO requestDTO);
    UrlCreateResponseDTO resolveShortUrl(String shortCode);
    UrlStatsResponseDTO getUrlStats(String shortCode);
    void deleteShortUrl(String shortCode);
    String getLongUrl(String shortCode);
}

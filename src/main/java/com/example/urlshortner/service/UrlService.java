package com.example.urlshortner.service;

import com.example.urlshortner.dto.request.UrlCreateRequestDTO;
import com.example.urlshortner.dto.response.UrlCreateResponseDTO;
import com.example.urlshortner.dto.response.UrlStatsResponseDTO;
import com.example.urlshortner.entity.Url;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UrlService {
    UrlCreateResponseDTO createShortUrl(UrlCreateRequestDTO requestDTO);
    UrlCreateResponseDTO resolveShortUrl(String shortCode);
    UrlStatsResponseDTO getUrlStats(String shortCode);
    void deleteShortUrl(String shortCode);
    String getLongUrl(String shortCode);
    void incrementClickCount(String shortCode);
    Page<Url> getUrlsByUserEmail(String email, Pageable pageable);
}

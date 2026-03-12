package com.example.urlshortner.mapper;

import com.example.urlshortner.config.AppConfig;
import com.example.urlshortner.dto.request.UrlCreateRequestDTO;
import com.example.urlshortner.dto.response.UrlCreateResponseDTO;
import com.example.urlshortner.dto.response.UrlStatsResponseDTO;
import com.example.urlshortner.entity.Url;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UrlMapper {
    private final AppConfig appConfig;

    public UrlMapper(AppConfig appConfig){
        this.appConfig = appConfig;
    }

    public Url toEntity(UrlCreateRequestDTO dto) {
        // convert dto → entity
        Url url = new Url();
        url.setLongUrl(dto.getLongUrl());
        url.setExpiryDate(dto.getExpiryDate());
        url.setClickCount(0);
        url.setCreatedAt(LocalDateTime.now());
        url.setActive(true);
        return url;
    }

    public UrlCreateResponseDTO toCreateResponse(Url url) {
        // entity → response
        String shortUrl = appConfig.getBaseUrl() + "/" + url.getShortCode();
        return new UrlCreateResponseDTO(
                shortUrl,
                url.getShortCode(),
                url.getLongUrl(),
                url.getExpiryDate()
        );
    }

    public UrlStatsResponseDTO toStatsResponse(Url url) {
        // entity → stats response
        return new UrlStatsResponseDTO(
                url.getLongUrl(),
                url.getShortCode(),
                url.getClickCount(),
                url.getCreatedAt(),
                url.getExpiryDate()
        );
    }
}

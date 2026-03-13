package com.example.urlshortner.service;

public interface RedisService {
    String getUrl(String shortCode);

    void saveUrl(String shortCode, String longUrl);

    void deleteUrl(String shortCode);

}

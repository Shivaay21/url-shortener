package com.example.urlshortner.service.Impl;

import com.example.urlshortner.service.RedisService;
import jakarta.transaction.Transactional;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    public RedisServiceImpl(RedisTemplate<String, String> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    private static final String KEY_PREFIX = "url:";

    private String buildKey(String shortCode){
        return KEY_PREFIX + shortCode;
    }

    @Override
    public String getUrl(String shortCode){
        String value = redisTemplate.opsForValue().get(buildKey(shortCode));
        return value;
    }

    @Override
    public void saveUrl(String shortCode, String longUrl, long ttlSeconds){
        redisTemplate.opsForValue().set(buildKey(shortCode),longUrl, ttlSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void deleteUrl(String shortCode){
        redisTemplate.delete(buildKey(shortCode));
    }
}

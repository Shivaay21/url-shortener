package com.example.urlshortner.service.Impl;

import com.example.urlshortner.service.RedisService;
import jakarta.transaction.Transactional;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static org.apache.logging.log4j.util.Lazy.value;

@Service
@Transactional
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisServiceImpl(RedisTemplate<String, String> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    private static final String KEY_PREFIX = "url::";

    private String buildKey(String shortCode){
        return KEY_PREFIX + shortCode;
    }

    @Override
    public String getUrl(String shortCode){
        String value = redisTemplate.opsForValue().get(buildKey(shortCode));
        return value != null ? value.toString() : null;
    }

    @Override
    public void saveUrl(String shortCode, String longUrl){
        redisTemplate.opsForValue().set(buildKey(shortCode),longUrl, 7, TimeUnit.DAYS);
    }

    @Override
    public void deleteUrl(String shortCode){
        redisTemplate.delete(buildKey(shortCode));
    }
}

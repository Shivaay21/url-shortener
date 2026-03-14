package com.example.urlshortner.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {
    private final Map<String, Bucket>buckets = new ConcurrentHashMap<>();

    private static final long CAPACITY = 10;
    private static final Duration REFILL_DURATION = Duration.ofMinutes(1);

    private Bucket createBucket(){
        Refill refill = Refill.intervally(CAPACITY, REFILL_DURATION);
        Bandwidth limit = Bandwidth.classic(CAPACITY, refill);
        return Bucket.builder().addLimit(limit).build();
    }

    public boolean tryConsume(String ip){
        Bucket bucket = buckets.computeIfAbsent(ip, k -> createBucket());
        return bucket.tryConsume(1);
    }

}

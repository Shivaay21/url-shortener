package com.example.urlshortner.schedule;

import com.example.urlshortner.entity.Url;
import com.example.urlshortner.repository.UrlRepository;
import com.example.urlshortner.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class ExpiredUrlScheduler {
    private final UrlRepository urlRepository;
    private final RedisService redisService;

    public ExpiredUrlScheduler(UrlRepository urlRepository, RedisService redisService){
        this.urlRepository = urlRepository;
        this.redisService = redisService;
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void deactivateExpiredUrls(){
        List<Url> expiredUrls = urlRepository.findAll().stream()
                .filter(url -> url.isActive() && url.getExpiryDate().isBefore(LocalDateTime.now()))
                .toList();

        for(Url url : expiredUrls){
            url.setActive(false);
            urlRepository.save(url);
            redisService.deleteUrl(url.getShortCode());
        }
        log.info("Expired URLs deactivated: {}", expiredUrls.size());
    }
}

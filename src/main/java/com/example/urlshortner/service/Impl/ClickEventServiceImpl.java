package com.example.urlshortner.service.Impl;

import com.example.urlshortner.entity.ClickEvent;
import com.example.urlshortner.repository.ClickEventRepository;
import com.example.urlshortner.service.ClickEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
public class ClickEventServiceImpl implements ClickEventService{
    private final ClickEventRepository clickEventRepository;

    public ClickEventServiceImpl(ClickEventRepository clickEventRepository){
        this.clickEventRepository = clickEventRepository;
    }

    @Override
    public void recordClick(String shortCode, String ipAddress, String userAgent){

        log.info("Recording clicks for {}", shortCode);

        ClickEvent click = new ClickEvent();
        click.setShortCode(shortCode);
        click.setIpAddress(ipAddress);
        click.setUserAgent(userAgent);
        click.setTimestamp(LocalDateTime.now());
        clickEventRepository.save(click);
    }

    @Override
    public int getTotalClicks(String shortCode){
        return clickEventRepository.countTotalClicks(shortCode);
    }

    @Override
    public int getClicksToday(String shortCode){
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        return clickEventRepository.countClicksSince(shortCode, startOfDay);
    }
}

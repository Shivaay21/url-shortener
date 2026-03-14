package com.example.urlshortner.service;

public interface ClickEventService {

    void recordClick(String shortCode, String ipAddress, String userAgent);

    int getTotalClicks(String shortCode);

    int getClicksToday(String shortCode);
}

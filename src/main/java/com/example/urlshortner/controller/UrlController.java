package com.example.urlshortner.controller;

import com.example.urlshortner.dto.request.UrlCreateRequestDTO;
import com.example.urlshortner.dto.response.ApiResponse;
import com.example.urlshortner.dto.response.UrlAnalyticsResponseDTO;
import com.example.urlshortner.dto.response.UrlCreateResponseDTO;
import com.example.urlshortner.dto.response.UrlStatsResponseDTO;
import com.example.urlshortner.service.ClickEventService;
import com.example.urlshortner.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/urls")
public class UrlController {
    private final UrlService urlService;
    private final ClickEventService clickEventService;


    public UrlController(UrlService urlService, ClickEventService clickEventService){
        this.urlService = urlService;
        this.clickEventService = clickEventService;
    }

    @PostMapping
    @Operation(summary = "Create a short URL")
    public ResponseEntity<ApiResponse<UrlCreateResponseDTO>> createShortUrl(@Valid @RequestBody UrlCreateRequestDTO requestDTO){
        UrlCreateResponseDTO responseDTO = urlService.createShortUrl(requestDTO);
        return ResponseEntity.ok(new ApiResponse<>(true, responseDTO, "URL created successfully"));
    }

    @GetMapping("/{shortCode}")
    @Operation(summary = "Resolve a short URL")
    public ResponseEntity<ApiResponse<UrlCreateResponseDTO>> resolveShortUrl(@PathVariable String shortCode){
        UrlCreateResponseDTO shortUrl = urlService.resolveShortUrl(shortCode);
        return ResponseEntity.ok(new ApiResponse<>(true, shortUrl, "URL resolved successfully"));
    }
    @GetMapping("/{shortCode}/stats")
    @Operation(summary = "Get URL statistics")
    public ResponseEntity<ApiResponse<UrlStatsResponseDTO>> getUrlStats(@PathVariable String shortCode){
        UrlStatsResponseDTO stats = urlService.getUrlStats(shortCode);
        return ResponseEntity.ok(
                new ApiResponse<>(true, stats, "Stats fetched successfully")
        );
    }

    @DeleteMapping("/{shortCode}")
    @Operation(summary = "Delete a URL")
    public ResponseEntity<ApiResponse<Void>> deleteShortUrl(@PathVariable String shortCode){
        urlService.deleteShortUrl(shortCode);
        return ResponseEntity.ok(new ApiResponse<>(true, null, "URL deleted successfully"));
    }

    @GetMapping("/{shortCode}/analytics")
    @Operation(summary = "Get a detailed analytics")
    public ResponseEntity<ApiResponse<UrlAnalyticsResponseDTO>> getAnalytics(@PathVariable String shortCode){
        int totalClicks = clickEventService.getTotalClicks(shortCode);
        int clicksToday = clickEventService.getClicksToday(shortCode);

        UrlAnalyticsResponseDTO analytics = new UrlAnalyticsResponseDTO(shortCode, totalClicks, clicksToday);
        return ResponseEntity.ok(new ApiResponse<>(true, analytics, "Analytics fetched successfully"));
    }
}

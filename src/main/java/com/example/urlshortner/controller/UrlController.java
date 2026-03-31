package com.example.urlshortner.controller;

import com.example.urlshortner.dto.request.UrlCreateRequestDTO;
import com.example.urlshortner.dto.response.ApiResponse;
import com.example.urlshortner.dto.response.UrlAnalyticsResponseDTO;
import com.example.urlshortner.dto.response.UrlCreateResponseDTO;
import com.example.urlshortner.dto.response.UrlStatsResponseDTO;
import com.example.urlshortner.entity.Url;
import com.example.urlshortner.mapper.UrlMapper;
import com.example.urlshortner.service.ClickEventService;
import com.example.urlshortner.service.UrlService;
import com.example.urlshortner.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/urls")
public class UrlController {
    private final UrlService urlService;
    private final ClickEventService clickEventService;
    private final UrlMapper urlMapper;


    public UrlController(UrlService urlService, ClickEventService clickEventService, UrlMapper urlMapper){
        this.urlService = urlService;
        this.clickEventService = clickEventService;
        this.urlMapper = urlMapper;
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

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Page<UrlCreateResponseDTO>>> getMyUrls(@RequestParam(defaultValue = "0")int page,
                                                                            @RequestParam(defaultValue = "10")int size
    ){
        String email = SecurityUtil.getCurrentUserEmail();
        Pageable pageable = PageRequest.of(page,size);

        Page<Url> urlPage = urlService.getUrlsByUserEmail(email,pageable);
        Page<UrlCreateResponseDTO> response =
                urlPage.map(urlMapper::toCreateResponse);

        return ResponseEntity.ok(
                new ApiResponse<>(true, response, "Url fetched successfully")
        );
    }
}

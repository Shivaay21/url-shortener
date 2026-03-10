package com.example.urlshortner.controller;

import com.example.urlshortner.dto.request.UrlCreateRequestDTO;
import com.example.urlshortner.dto.response.UrlCreateResponseDTO;
import com.example.urlshortner.dto.response.UrlStatsResponseDTO;
import com.example.urlshortner.entity.Url;
import com.example.urlshortner.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shorten")
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService){
        this.urlService = urlService;
    }

    @PostMapping
    public ResponseEntity<UrlCreateResponseDTO> createShortUrl(@Valid @RequestBody UrlCreateRequestDTO requestDTO){
        UrlCreateResponseDTO created = urlService.createShortUrl(requestDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<UrlCreateResponseDTO> resolveShortUrl(@PathVariable String shortCode){
        UrlCreateResponseDTO shortUrl = urlService.resolveShortUrl(shortCode);
        return ResponseEntity.ok(shortUrl);
    }
    @GetMapping("/url/{shortCode}/stats")
    public ResponseEntity<UrlStatsResponseDTO> getUrlStats(@PathVariable String shortCode){
        UrlStatsResponseDTO stats = urlService.getUrlStats(shortCode);
        return ResponseEntity.ok(stats);
    }

    @DeleteMapping("/{shortCode}")
    public ResponseEntity<Void> deleteShortUrl(@PathVariable String shortCode){
        urlService.deleteShortUrl(shortCode);
        return ResponseEntity.noContent().build();
    }
}

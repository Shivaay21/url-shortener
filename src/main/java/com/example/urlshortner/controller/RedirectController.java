package com.example.urlshortner.controller;

import com.example.urlshortner.service.ClickEventService;
import com.example.urlshortner.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RestController
public class RedirectController {
    private final UrlService urlService;
    private final ClickEventService clickEventService;

    public RedirectController(UrlService urlService, ClickEventService clickEventService){
        this.urlService = urlService;
        this.clickEventService = clickEventService;
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode,
                                         HttpServletRequest request){

        log.info("Redirect request received for {}", shortCode);
        String  longUrl = urlService.getLongUrl(shortCode);

        clickEventService.recordClick(
                shortCode,
                request.getRemoteAddr(),
                request.getHeader("User-Agent")
        );

        log.info("Redirecting {} to {}", shortCode, longUrl);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(longUrl))
                .build();
    }

}

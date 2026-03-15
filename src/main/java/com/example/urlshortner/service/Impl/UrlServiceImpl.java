package com.example.urlshortner.service.Impl;

import com.example.urlshortner.dto.request.UrlCreateRequestDTO;
import com.example.urlshortner.dto.response.UrlCreateResponseDTO;
import com.example.urlshortner.dto.response.UrlStatsResponseDTO;
import com.example.urlshortner.entity.Url;
import com.example.urlshortner.exception.AliasAlreadyExistsException;
import com.example.urlshortner.exception.InvalidUrlException;
import com.example.urlshortner.exception.UrlExpiredException;
import com.example.urlshortner.exception.UrlNotFoundException;
import com.example.urlshortner.mapper.UrlMapper;
import com.example.urlshortner.repository.UrlRepository;
import com.example.urlshortner.service.RedisService;
import com.example.urlshortner.service.UrlService;
import com.example.urlshortner.util.Base62Encoder;
import com.example.urlshortner.util.UrlValidator;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
public class UrlServiceImpl implements UrlService{
    private final UrlRepository urlRepository;
    private final Base62Encoder base62Encoder;
    private final UrlMapper urlMapper;
    private final RedisService redisService;

    public UrlServiceImpl(UrlRepository urlRepository, Base62Encoder base62Encoder,
                          UrlMapper urlMapper, RedisService redisService){
        this.urlRepository = urlRepository;
        this.base62Encoder = base62Encoder;
        this.urlMapper = urlMapper;
        this.redisService = redisService;
    }

    @Override
    public UrlCreateResponseDTO createShortUrl(UrlCreateRequestDTO requestDTO){
        log.info("Creating short URL for {}", requestDTO.getLongUrl());

        if(!UrlValidator.isValid(requestDTO.getLongUrl())){
            log.warn("Invalid URL Format {}", requestDTO.getLongUrl());
            throw new InvalidUrlException("Invalid URL Format");
        }

        if(requestDTO.getExpiryDate()==null){
            requestDTO.setExpiryDate(LocalDateTime.now().plusDays(7));
        }

        String shortCode;

        if(requestDTO.getCustomAlias() != null && !requestDTO.getCustomAlias().isBlank()) {
            shortCode = requestDTO.getCustomAlias();
            log.info("Using custom alias {}", shortCode);

            if (urlRepository.existsByShortCode(shortCode)) {
                log.warn("Alias already exists {}", shortCode);
                throw new AliasAlreadyExistsException("Alias already exists");
            }

            Url url = urlMapper.toEntity(requestDTO);
            url.setShortCode(shortCode);

            Url savedUrl = urlRepository.save(url);

            return urlMapper.toCreateResponse(savedUrl);
        }
        else{

            Url url = urlMapper.toEntity(requestDTO);
            url.setShortCode("temp");

            Url savedUrl = urlRepository.save(url);

            shortCode = base62Encoder.encode(savedUrl.getId());

            savedUrl.setShortCode(shortCode);

            urlRepository.save(savedUrl);

            return urlMapper.toCreateResponse(savedUrl);
        }
    }

    @Override
    public UrlCreateResponseDTO resolveShortUrl(String shortCode){
        log.info("Resolving short code {}", shortCode);

        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> {
                    log.warn("URL not found {}", shortCode);
                    return new UrlNotFoundException("Url not found");
                });

        if(!url.isActive()){
            log.warn("URL is inactive {}", shortCode);
            throw new UrlNotFoundException("URL Deleted");
        }

        if(url.getExpiryDate().isBefore(LocalDateTime.now())){
            log.warn("URL expired for shortCode {}", shortCode);
            throw new UrlExpiredException("URL expired");
        }

        return urlMapper.toCreateResponse(url);
    }

    @Override
    public UrlStatsResponseDTO getUrlStats(String shortCode){
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("Url not found"));
        if(url.getExpiryDate().isBefore(LocalDateTime.now())){
            log.warn("URL expired for shortCode {}", shortCode);
            throw new UrlExpiredException("URL Expired");
        }
        if(!url.isActive()){
            log.warn("URL is inactive {}", shortCode);
            throw new UrlNotFoundException("URL deleted");
        }
        return urlMapper.toStatsResponse(url);
    }

    @Override
    public void deleteShortUrl(String shortCode){
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("Url not found"));
        url.setActive(false);
        urlRepository.save(url);
        redisService.deleteUrl(shortCode);
        log.info("Deleted short URL {}", shortCode);
    }

    @Override
    public String getLongUrl(String shortCode){
        String cachedUrl = (String) redisService.getUrl(shortCode);

        if(cachedUrl != null){
            log.info("Cache hit for shortCode {}", shortCode);
            return cachedUrl;
        }

        log.info("Cache miss for shortCode {}, fetching from DB", shortCode);

        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("URL not found"));

        if(!url.isActive()){
            log.warn("URL is inactive {}", shortCode);
            throw new UrlNotFoundException("URL Deleted");
        }

        if(url.getExpiryDate().isBefore(LocalDateTime.now())){
            log.warn("URL expired for shortCode {}", shortCode);
            throw new UrlExpiredException("URL Expired");
        }

        url.setClickCount(url.getClickCount() + 1);
        urlRepository.save(url);

        long ttl = Duration.between(LocalDateTime.now(), url.getExpiryDate()).toSeconds();

        if(ttl > 0){
            redisService.saveUrl(shortCode, url.getLongUrl(), ttl);
        }

        return url.getLongUrl();
    }

    @Override
    public void incrementClickCount(String shortCode){
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("URL not found"));
        url.setClickCount(url.getClickCount()+1);
        urlRepository.save(url);
    }
}

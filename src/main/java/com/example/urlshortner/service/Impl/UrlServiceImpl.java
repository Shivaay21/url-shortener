package com.example.urlshortner.service.Impl;

import com.example.urlshortner.dto.request.UrlCreateRequestDTO;
import com.example.urlshortner.dto.response.UrlCreateResponseDTO;
import com.example.urlshortner.dto.response.UrlStatsResponseDTO;
import com.example.urlshortner.entity.Url;
import com.example.urlshortner.entity.User;
import com.example.urlshortner.exception.*;
import com.example.urlshortner.mapper.UrlMapper;
import com.example.urlshortner.repository.UrlRepository;
import com.example.urlshortner.repository.UserRepository;
import com.example.urlshortner.service.RedisService;
import com.example.urlshortner.service.UrlService;
import com.example.urlshortner.util.Base62Encoder;
import com.example.urlshortner.util.SecurityUtil;
import com.example.urlshortner.util.UrlValidator;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final UserRepository userRepository;

    public UrlServiceImpl(UrlRepository urlRepository, Base62Encoder base62Encoder,
                          UrlMapper urlMapper, RedisService redisService, UserRepository userRepository){
        this.urlRepository = urlRepository;
        this.base62Encoder = base62Encoder;
        this.urlMapper = urlMapper;
        this.redisService = redisService;
        this.userRepository = userRepository;
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

            String email = SecurityUtil.getCurrentUserEmail();

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            url.setUser(user);

            try {
                Url savedUrl = urlRepository.save(url);
                long ttl = Duration.between(LocalDateTime.now(), savedUrl.getExpiryDate()).toSeconds();
                try {
                    if(ttl > 0){
                        redisService.saveUrl(shortCode, savedUrl.getLongUrl(), ttl);
                    }
                } catch (Exception e) {
                    log.warn("Redis unavailable, skipping cache");
                }
                return urlMapper.toCreateResponse(savedUrl);

            }catch (DataIntegrityViolationException ex){
                throw new AliasAlreadyExistsException("Alias already exists");
            }
        }
        else{

            Url url = urlMapper.toEntity(requestDTO);
            url.setShortCode("temp");

            String email = SecurityUtil.getCurrentUserEmail();

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            url.setUser(user);

            Url savedUrl = urlRepository.save(url);

            shortCode = base62Encoder.encode(savedUrl.getId());

            savedUrl.setShortCode(shortCode);

            urlRepository.save(savedUrl);

            long ttl = Duration.between(LocalDateTime.now(), savedUrl.getExpiryDate()).toSeconds();

            try {
                if(ttl > 0){
                    redisService.saveUrl(shortCode, savedUrl.getLongUrl(), ttl);
                }
            } catch (Exception e) {
                log.warn("Redis Unavailable, skipping cache");
            }

            return urlMapper.toCreateResponse(savedUrl);
        }
    }

    @Override
    public UrlCreateResponseDTO resolveShortUrl(String shortCode){
        log.info("Resolving short code {}", shortCode);
        Url url = validateOwnerUrl(shortCode);

        return urlMapper.toCreateResponse(url);
    }

    @Override
    public UrlStatsResponseDTO getUrlStats(String shortCode){
        Url url = validateOwnerUrl(shortCode);
        return urlMapper.toStatsResponse(url);
    }

    @Override
    public void deleteShortUrl(String shortCode){
        Url url = validateOwnerUrl(shortCode);

        url.setActive(false);
        urlRepository.save(url);
        redisService.deleteUrl(shortCode);

        log.info("Deleted short URL {}", shortCode);
    }

    @Override
    public String getLongUrl(String shortCode){
        String cachedUrl = null;
        try {
            cachedUrl = redisService.getUrl(shortCode);
        } catch (Exception e) {
            log.warn("Redis unavailable, fallback to DB");
        }

        if(cachedUrl != null){
            log.info("Cache hit for shortCode {}", shortCode);
            validatePublicUrl(shortCode);
            return cachedUrl;
            }

        log.info("Cache miss for shortCode {}, fetching from DB", shortCode);
        Url url = validatePublicUrl(shortCode);

        long ttl = Duration.between(LocalDateTime.now(), url.getExpiryDate()).toSeconds();

        try {
            if(ttl > 0){
                redisService.saveUrl(shortCode, url.getLongUrl(), ttl);
            }
        } catch (Exception e) {
            log.warn("Redis unavailable, skipping cache");
        }

        return url.getLongUrl();
    }

    @Override
    public void incrementClickCount(String shortCode){
        urlRepository.incrementClickCount(shortCode);
    }

    private Url validatePublicUrl(String shortCode){
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("URL not found"));

        if(!url.isActive()){
            throw new UrlNotFoundException("URL Deleted");
        }

        if(url.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new UrlExpiredException("URL Expired");
        }

        return url;
    }

    private Url validateOwnerUrl(String shortCode){
        Url url = validatePublicUrl(shortCode);

        String email = SecurityUtil.getCurrentUserEmail();

        if(!url.getUser().getEmail().equals(email)){
            throw new UnauthorizedException("You are not allowed to access this URL");
        }
        return url;
    }

    @Override
    public Page<Url> getUrlsByUserEmail(String email, Pageable pageable){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return urlRepository.findAllByUser(user, pageable);
    }
}

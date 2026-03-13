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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
        if(!UrlValidator.isValid(requestDTO.getLongUrl())){
            throw new InvalidUrlException("Invalid URL Format");
        }

        if(requestDTO.getExpiryDate()==null){
            requestDTO.setExpiryDate(LocalDateTime.now().plusDays(7));
        }

        String shortCode;

        if(requestDTO.getCustomAlias() != null && !requestDTO.getCustomAlias().isBlank()){
            shortCode = requestDTO.getCustomAlias();

            if(urlRepository.existsByShortCode(shortCode)){
                throw new AliasAlreadyExistsException("Alias already exists");
            }
        }
        else{
            shortCode = base62Encoder.encode(System.currentTimeMillis());
            while(urlRepository.existsByShortCode(shortCode)){
                shortCode = base62Encoder.encode(System.currentTimeMillis());
            }
        }
        Url url = urlMapper.toEntity(requestDTO);
        url.setShortCode(shortCode);
        Url savedUrl = urlRepository.save(url);

        return urlMapper.toCreateResponse(savedUrl);
    }

    @Override
    public UrlCreateResponseDTO resolveShortUrl(String shortCode){
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("Url not found"));

        if(!url.isActive()){
            throw new UrlNotFoundException("URL Deleted");
        }

        if(url.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new UrlExpiredException("URL expired");
        }
        url.setClickCount(url.getClickCount() +1 );
        urlRepository.save(url);
        return urlMapper.toCreateResponse(url);
    }

    @Override
    public UrlStatsResponseDTO getUrlStats(String shortCode){
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("Url not found"));
        if(url.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new UrlExpiredException("URL Expired");
        }
        if(!url.isActive()){
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
    }

    @Override
    public String getLongUrl(String shortCode){
        String cachedUrl = (String) redisService.getUrl(shortCode);

        if(cachedUrl != null){
            return cachedUrl;
        }

        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("URL not found"));

        if(!url.isActive()){
            throw new UrlNotFoundException("URL Deleted");
        }

        if(url.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new UrlExpiredException("URL Expired");
        }

        url.setClickCount(url.getClickCount() + 1);
        urlRepository.save(url);

        redisService.saveUrl(shortCode, url.getLongUrl());

        return url.getLongUrl();
    }
}

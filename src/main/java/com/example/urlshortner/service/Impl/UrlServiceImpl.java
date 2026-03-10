package com.example.urlshortner.service.Impl;

import com.example.urlshortner.dto.request.UrlCreateRequestDTO;
import com.example.urlshortner.dto.response.UrlCreateResponseDTO;
import com.example.urlshortner.dto.response.UrlStatsResponseDTO;
import com.example.urlshortner.entity.Url;
import com.example.urlshortner.mapper.UrlMapper;
import com.example.urlshortner.repository.UrlRepository;
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

    public UrlServiceImpl(UrlRepository urlRepository, Base62Encoder base62Encoder, UrlMapper urlMapper){
        this.urlRepository = urlRepository;
        this.base62Encoder = base62Encoder;
        this.urlMapper = urlMapper;
    }

    @Override
    public UrlCreateResponseDTO createShortUrl(UrlCreateRequestDTO requestDTO){
        if(!UrlValidator.isValid(requestDTO.getLongUrl())){
            throw new RuntimeException("Invalid URL Format");
        }

        Url url = urlMapper.toEntity(requestDTO);
        Url savedUrl = urlRepository.save(url);

        String shortCode;
        if(requestDTO.getCustomAlias() != null && !requestDTO.getCustomAlias().isBlank()){
            shortCode = requestDTO.getCustomAlias();
        }
        else{
            shortCode = base62Encoder.encode(savedUrl.getId());
        }
        savedUrl.setShortCode(shortCode);
        urlRepository.save(savedUrl);
        return urlMapper.toCreateResponse(savedUrl);
    }

    @Override
    public UrlCreateResponseDTO resolveShortUrl(String shortCode){
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("Url not found"));

        if(!url.isActive()){
            throw new RuntimeException("URL Deleted");
        }

        if(url.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new RuntimeException("URL expired");
        }
        url.setClickCount(url.getClickCount() +1 );
        return urlMapper.toCreateResponse(url);
    }

    @Override
    public UrlStatsResponseDTO getUrlStats(String shortCode){
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("Url not found"));
        if(!url.isActive()){
            throw new RuntimeException("URL is deleted");
        }
        return urlMapper.toStatsResponse(url);
    }

    @Override
    public void deleteShortUrl(String shortCode){
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("Url not found"));
        url.setActive(false);
        urlRepository.save(url);
    }

    @Override
    public int incrementClickCount(String shortCode){
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("Url not found"));
        url.setClickCount(url.getClickCount() + 1);
        urlRepository.save(url);
        return url.getClickCount();
    }
}

package com.example.urlshortner.util;

import com.example.urlshortner.service.RateLimiterService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class RateLimiterFilter extends OncePerRequestFilter {
    private final RateLimiterService rateLimiterService;

    public RateLimiterFilter(RateLimiterService rateLimiterService){
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)throws ServletException, IOException {
        if(request.getMethod().equalsIgnoreCase("POST") &&
        request.getRequestURI().startsWith("/api/urls")){
            String clientIp = getClientIP(request);

            boolean allowed = rateLimiterService.tryConsume(clientIp);

            if(!allowed){
                log.warn("Rate limit exceeded for IP {} to endpoint {}", clientIp, request.getRequestURI());
                response.setStatus(429);
                response.getWriter().write("Too Many Requests. Try again later.");
                return;
            }
        }
        filterChain.doFilter(request,response);
    }
    private String getClientIP(HttpServletRequest request){
        String xfHeader = request.getHeader("X-Forwarded-For");
        if(xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

}

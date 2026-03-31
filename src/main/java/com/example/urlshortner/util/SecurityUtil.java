package com.example.urlshortner.util;

import com.example.urlshortner.exception.UnauthorizedException;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static String getCurrentUserEmail(){
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth == null || auth.getPrincipal().equals("anonymousUser")){
            throw new UnauthorizedException("User not authenticated");
        }

        return auth.getName();
    }
}

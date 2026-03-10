package com.example.urlshortner.util;

import java.net.URL;

public class UrlValidator {
    public static boolean isValid(String url){
        try {
            new URL(url).toURI();
            return true;
        }catch (Exception e){
            return false;
        }
    }
}

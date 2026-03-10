package com.example.urlshortner.util;

import org.springframework.stereotype.Component;

@Component
public class Base62Encoder {
    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = 62;

    public String encode(long number){
        if(number == 0)
            return "0";

        StringBuilder result = new StringBuilder();
        while(number > 0){
            int remainder = (int) (number%BASE);
            result.append(BASE62.charAt(remainder));
            number = number/BASE;
        }
        return result.reverse().toString();
    }

    public long decode(String shortCode){
        long result = 0;
        for(char ch : shortCode.toCharArray()){
            int index = BASE62.indexOf(ch);
            result = result * BASE + index;
        }
        return result;
    }
}

package com.vce.vce._shared.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;

public class SecurityUtils {
    public static String getFingerprint(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("fingerprint"))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}

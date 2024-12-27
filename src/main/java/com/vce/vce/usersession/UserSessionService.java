package com.vce.vce.usersession;

import com.vce.vce.token.access.AccessTokenService;
import com.vce.vce.token.refresh.RefreshTokenService;
import com.vce.vce.user.User;
import com.vce.vce.usersession.dto.CreateUserSessionDTO;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserSessionService {
    private final UserSessionRepository userSessionRepository;
    private final HttpServletRequest httpServletRequest;
    private final RefreshTokenService refreshTokenService;
    private final AccessTokenService accessTokenService;

    @Transactional
    public UserSession createUserSession(CreateUserSessionDTO createDTO) {
        UserSession session =  UserSession.builder()
                .user(createDTO.user())
                .lastActivity(LocalDateTime.now())
                .deviceInfo(getDeviceInfo())
                .lastActivity(LocalDateTime.now())
                .ipAddress(getClientIpAddress())
                .fingerprint(getFingerprint())
                .build();

        return userSessionRepository.save(session);
    }

    private String getDeviceInfo() {
        UserAgent userAgent = UserAgent.parseUserAgentString(httpServletRequest.getHeader("User-Agent"));

        String deviceInfo = userAgent.getBrowser().getName();
        if(userAgent.getBrowserVersion() != null) {
            deviceInfo += " " + userAgent.getBrowserVersion().getVersion();
        }

        deviceInfo += " " + userAgent.getOperatingSystem().getName();
        deviceInfo += " " + userAgent.getOperatingSystem().getDeviceType();

        return deviceInfo;
    }

    private String getClientIpAddress() {
        String[] IP_HEADERS = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED",
                "HTTP_VIA",
                "REMOTE_ADDR"
        };

        for (String header : IP_HEADERS) {
            String value = httpServletRequest.getHeader(header);
            if (value != null && !value.isEmpty() && !"unknown".equalsIgnoreCase(value)) {
                // X-Forwarded-For may contain several IPs, we take the first one
                String[] parts = value.split("\\s*,\\s*");
                return parts[0];
            }
        }

        return httpServletRequest.getRemoteAddr();
    }

    private String getFingerprint() {
        Cookie cookie =  Arrays.stream(httpServletRequest.getCookies())
                .filter(c -> c.getName().equals("fingerprint"))
                .findFirst()
                .orElseThrow(() -> new AccessDeniedException("Full authentication is required to access this resource"));

        return cookie.getValue();
    }

    @Transactional
    public void deactivateSessionCompletely(UserSession userSession) {
        userSession.setIsActive(false);

        refreshTokenService.revokeActiveTokens(userSession);
        accessTokenService.revokeActiveTokens(userSession);

        userSessionRepository.save(userSession);
    }

    @Transactional
    public void deactivatePreviousFingerprintSessions(User user, String fingerprint) {
        userSessionRepository.deactivateSessionsByFingerprintAndUser(fingerprint, user);
    }

}

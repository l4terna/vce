package com.vce.vce.usersession;

import com.vce.vce._shared.model.dto.PageableDTO;
import com.vce.vce.token.access.AccessTokenService;
import com.vce.vce.token.refresh.RefreshTokenService;
import com.vce.vce.user.User;
import com.vce.vce.usersession.dto.CreateUserSessionDTO;
import com.vce.vce.usersession.dto.UserSessionDTO;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;


@Service
@RequiredArgsConstructor
public class UserSessionService {
    private final UserSessionRepository userSessionRepository;
    private final HttpServletRequest httpServletRequest;
    private final RefreshTokenService refreshTokenService;
    private final AccessTokenService accessTokenService;
    private final UserSessionMapper userSessionMapper;

    @Transactional
    public UserSession createUserSession(CreateUserSessionDTO createDTO, String fingerprint) {
        UserSession session =  UserSession.builder()
                .user(createDTO.user())
                .lastActivity(LocalDateTime.now())
                .deviceInfo(getDeviceInfo())
                .lastActivity(LocalDateTime.now())
                .ipAddress(getClientIpAddress())
                .fingerprint(fingerprint)
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

    public String getFingerprint() {
        if (httpServletRequest.getCookies() == null) {
            return null;
        }

        Cookie cookie =  Arrays.stream(httpServletRequest.getCookies())
                .filter(c -> c.getName().equals("__fprid"))
                .findFirst()
                .orElse(null);

        return cookie == null ? null : cookie.getValue();
    }

    @Transactional
    public void deactivateSessionCompletely(UserSession userSession) {
        userSession.setIsActive(false);

        refreshTokenService.revokeActiveTokens(userSession);
        accessTokenService.revokeActiveTokens(userSession);

        userSessionRepository.save(userSession);
    }

    @Transactional
    public void deactivatePreviousSessions(User user, String fingerprint) {
        userSessionRepository.deactivateSessionsByFingerprintAndUser(fingerprint, user);
        accessTokenService.revokeActiveTokensByFingerprintAndUser(fingerprint, user);
        refreshTokenService.revokeActiveTokensByFingerprintAndUser(fingerprint, user);
    }

    @Transactional(readOnly = true)
    public Page<UserSessionDTO> getUserSessions(PageableDTO pageableDTO, User currentUser) {
        return userSessionRepository.findAllAnotherActiveSessions(pageableDTO.toPageable(), findUserSessionByUser(currentUser))
                .map(userSessionMapper::toDTO);
    }

    @Transactional
    public void deactivateSession(Long id) {
        UserSession userSession = userSessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Session not found"));
        userSession.setIsActive(false);
        userSessionRepository.save(userSession);
    }

    @Transactional
    public void deactivateAllOtherUserSessions(User currentUser) {
        UserSession userSession = findUserSessionByUser(currentUser);
        userSessionRepository.deactivateAllOtherSessions(userSession);
    }

    @Transactional(readOnly = true)
    public UserSession findUserSessionByUser(User user) {
        String fingerprint = getFingerprint();
        return userSessionRepository.findActiveByFingerprintAndUser(fingerprint, user)
                .orElseThrow(() -> new EntityNotFoundException("Session not found"));
    }
}

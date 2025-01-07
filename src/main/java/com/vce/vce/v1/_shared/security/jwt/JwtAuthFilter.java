package com.vce.vce.v1._shared.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vce.vce.v1._shared.exception.ErrorResponse;
import com.vce.vce.v1.token.access.AccessTokenService;
import com.vce.vce.v1.user.CustomUserDetailsService;
import com.vce.vce.v1.usersession.UserSessionService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final AccessTokenService accessTokenService;
    private final ObjectMapper objectMapper;
    private final UserSessionService userSessionService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            String fingerprint = userSessionService.getFingerprint();

            if (request.getRequestURI().contains("/auth/login") ||
                    request.getRequestURI().contains("/auth/register") ||
                    request.getRequestURI().contains("/auth/refresh") ||
                    request.getRequestURI().contains("/ws")) {
                filterChain.doFilter(request, response);
                return;
            }

            if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ") || fingerprint == null) {
                filterChain.doFilter(request, response);
                return;
            }

            String jwtToken = authHeader.substring(7);

            try {
                String username = jwtService.extractUsername(jwtToken);

                if (StringUtils.isEmpty(username)) {
                    throw new JwtException("Username is empty");
                }

                if (!accessTokenService.validateToken(jwtToken, fingerprint)) {
                    throw new JwtException("Token validation failed");
                }

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

            } catch (Exception e) {
                handleError(request, response, e);
                return;
            }

            filterChain.doFilter(request, response);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, Exception ex)
            throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        String message = "Invalid credentials";

        if (ex instanceof ExpiredJwtException) {
            message = ex.getMessage();
        } else if (ex instanceof JwtException) {
            message = ex.getMessage();
        }

        ErrorResponse error = ErrorResponse.builder()
                .message(message)
                .type("Unauthorized")
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}

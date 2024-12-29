package com.vce.vce._shared.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vce.vce._shared.exception.ErrorResponse;
import com.vce.vce._shared.security.SecurityUtils;
import com.vce.vce.token.access.AccessTokenService;
import com.vce.vce.user.CustomUserDetailsService;
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

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            String fingerprint = SecurityUtils.getFingerprint(request);

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

                if (!jwtService.isTokenValid(jwtToken, userDetails)) {
                    throw new JwtException("Token is not valid for user");
                }

                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);

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

        String message = ex.getMessage();
        String type = "Authentication failed";

        if (ex instanceof ExpiredJwtException) {
            type = "Token Expired";
        } else if (ex instanceof JwtException) {
            type = "Invalid Token";
        }

        ErrorResponse error = ErrorResponse.builder()
                .message(message)
                .type(type)
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}

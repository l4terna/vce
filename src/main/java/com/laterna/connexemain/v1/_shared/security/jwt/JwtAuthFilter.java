package com.laterna.connexemain.v1._shared.security.jwt;

import com.laterna.connexemain.v1.token.access.AccessTokenService;
import com.laterna.connexemain.v1.user.UserDetailsServiceImpl;
import com.laterna.connexemain.v1.usersession.UserSessionService;
import io.jsonwebtoken.JwtException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final AccessTokenService accessTokenService;
    private final UserSessionService userSessionService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
//            String fingerprint = userSessionService.getFingerprint();
            String fingerprint = "string";
            if (request.getRequestURI().contains("/auth/login") ||
                    request.getRequestURI().contains("/auth/register") ||
                    request.getRequestURI().contains("/auth/refresh")) {
                filterChain.doFilter(request, response);
                return;
            }

            if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
                throw new JwtException("Invalid credentials");
            }

            String jwtToken = authHeader.substring(7);

            String username = jwtService.extractUsername(jwtToken);

            if (StringUtils.isEmpty(username) || !accessTokenService.validateToken(jwtToken, fingerprint)) {
                throw new JwtException("Invalid token");
            }

            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

}

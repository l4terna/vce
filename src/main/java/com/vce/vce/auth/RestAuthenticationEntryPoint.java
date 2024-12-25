package com.vce.vce.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vce.vce._shared.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

   private final ObjectMapper objectMapper;

   @Override
   public void commence(HttpServletRequest request,
                        HttpServletResponse response,
                        AuthenticationException ex) throws IOException {
       
       response.setStatus(HttpStatus.UNAUTHORIZED.value());
       response.setContentType("application/json");
       response.setCharacterEncoding("UTF-8");
       
       ErrorResponse errorResponse = ErrorResponse.builder()
           .statusCode(HttpStatus.UNAUTHORIZED.value())
           .message(ex.getMessage())
           .type("Authentication failed")
           .path(request.getRequestURI())
           .timestamp(LocalDateTime.now())
           .build();

       String jsonResponse = objectMapper.writeValueAsString(errorResponse);
       response.getWriter().write(jsonResponse);
   }
}
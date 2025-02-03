package com.flux.flux.v1._shared.websocket.interceptor;

import com.flux.flux.v1._shared.security.jwt.JwtService;
import com.flux.flux.v1.user.User;
import com.flux.flux.v1.user.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ChannelAuthInterceptor implements ChannelInterceptor {
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            List<String> authorization = accessor.getNativeHeader("Authorization");

            if (authorization != null && !authorization.isEmpty()) {
                String token = authorization.get(0);

                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7);

                    try {
                        String username = jwtService.extractUsername(token);
                        User userDetails = userDetailsServiceImpl.loadUserByUsername(username);

                        if (userDetails != null) {
                            UsernamePasswordAuthenticationToken auth =
                                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            SecurityContextHolder.getContext().setAuthentication(auth);
                            accessor.setUser(auth);
                        }
                    } catch (Exception e) {
                        throw new MessagingException("Unauthorized");
                    }
                }
            }
        }

        return message;
    }
}

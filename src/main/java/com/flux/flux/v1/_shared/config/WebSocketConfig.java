package com.flux.flux.v1._shared.config;

import com.flux.flux.v1._shared.websocket.error.StompSubProtocolErrorHandlerImpl;
import com.flux.flux.v1._shared.websocket.interceptor.ChannelAuthInterceptor;
import com.flux.flux.v1._shared.websocket.interceptor.ChannelSubscriptionInterceptor;
import com.flux.flux.v1._shared.websocket.interceptor.HandshakeInterceptorImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final ChannelAuthInterceptor channelAuthInterceptor;
    private final HandshakeInterceptorImpl handshakeInterceptorImpl;
    private final ChannelSubscriptionInterceptor channelSubscriptionInterceptor;
    private final StompSubProtocolErrorHandlerImpl stompSubProtocolErrorHandlerImpl;

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app");
        config.enableSimpleBroker("/topic", "/queue", "/user");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins(allowedOrigins)
                .addInterceptors(handshakeInterceptorImpl)
                .withSockJS();

        registry.addEndpoint("/ws");
        registry.setErrorHandler(stompSubProtocolErrorHandlerImpl);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(channelAuthInterceptor);
        registration.interceptors(channelSubscriptionInterceptor);
    }

}

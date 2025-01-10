package com.flux.flux.v1._shared.config;

import com.flux.flux.v1._shared.websocket.interceptor.ChannelAuthInterceptor;
import com.flux.flux.v1._shared.websocket.interceptor.HandshakeInterceptorImpl;
import lombok.RequiredArgsConstructor;
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

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app");
        config.enableSimpleBroker("/topic", "/queue");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:63342")
                .addInterceptors(handshakeInterceptorImpl)
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(channelAuthInterceptor);
//        registration.interceptors(channelSubscriptionInterceptor);
    }

}

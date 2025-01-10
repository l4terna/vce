package com.flux.flux.v1._shared.websocket.interceptor;

import com.flux.flux.v1.hubs.Hub;
import com.flux.flux.v1.hubs.HubService;
import com.flux.flux.v1.permission.PermissionService;
import com.flux.flux.v1.permission.enumeration.Permission;
import com.flux.flux.v1.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ChannelSubscriptionInterceptor implements ChannelInterceptor {
    private final PermissionService permissionService;
    private final HubService hubService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String destination = accessor.getDestination();
            if (destination != null && destination.startsWith("/topic/channel/")) {
                User principal = (User) accessor.getUser();
                if (principal == null) {
                    throw new IllegalStateException("No authentication data found");
                }

                Long channelId = extractChannelId(destination);
                Hub hub = hubService.findHubByChannelId(channelId);

                permissionService.hasPermissionsThrow(principal.getId(), hub.getId(), Permission.SEND_MESSAGES);
            }
        }
        return message;
    }

    private Long extractChannelId(String destination) {
        String[] parts = destination.split("/");
        return Long.parseLong(parts[parts.length - 1]);
    }
}

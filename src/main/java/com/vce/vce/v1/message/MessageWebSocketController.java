package com.vce.vce.v1.message;

import com.vce.vce.v1.message.dto.CreateMessageDTO;
import com.vce.vce.v1.message.dto.MessageDTO;
import com.vce.vce.v1.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class MessageWebSocketController {
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/messages/{channelId}")
    @SendTo("/topic/channel/{channelId}")
    public MessageDTO handleMessage(
            @PathVariable Long channelId,
            CreateMessageDTO createMessageDTO,
            @AuthenticationPrincipal User user
    ) {
        return messageService.create(channelId, createMessageDTO, user);
    }
}

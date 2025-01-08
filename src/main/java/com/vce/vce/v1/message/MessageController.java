package com.vce.vce.v1.message;

import com.vce.vce.v1.message.dto.CreateMessageDTO;
import com.vce.vce.v1.message.dto.MessageDTO;
import com.vce.vce.v1.message.dto.UpdateMessageDTO;
import com.vce.vce.v1.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/channels/{channelId}/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping
    public ResponseEntity<MessageDTO> createMessage(
            @PathVariable Long channelId,
            @Valid @RequestBody CreateMessageDTO createMessageDTO,
            @AuthenticationPrincipal User user
    ) {
        MessageDTO message = messageService.create(channelId, createMessageDTO, user);

        messagingTemplate.convertAndSend(
                "/topic/channels/" + channelId + "/messages",
                message
        );

        return ResponseEntity.ok(message);
    }

    @PutMapping("/{messageId}")
    public ResponseEntity<MessageDTO> updateMessage(
            @PathVariable Long channelId,
            @PathVariable Long messageId,
            @Valid @RequestBody UpdateMessageDTO updateMessageDTO,
            @AuthenticationPrincipal User user
    ) {
        MessageDTO message = messageService.update(channelId, messageId, updateMessageDTO, user);

        messagingTemplate.convertAndSend(
                "/topic/channels/" + channelId + "/messages/update",
                message
        );

        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(
            @PathVariable Long channelId,
            @PathVariable Long messageId,
            @AuthenticationPrincipal User user
    ) {
        messageService.delete(channelId, messageId, user);

        messagingTemplate.convertAndSend(
                "/topic/channels/" + channelId + "/messages/delete",
                messageId
        );

        return ResponseEntity.noContent().build();
    }
}
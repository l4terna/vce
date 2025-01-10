package com.flux.flux.v1.channel;

import com.flux.flux.v1.channel.dto.ChannelDTO;
import com.flux.flux.v1.channel.dto.CreateDirectChannelDTO;
import com.flux.flux.v1.channel.dto.CreateHubChannelDTO;
import com.flux.flux.v1.channel.dto.UpdateChannelDTO;
import com.flux.flux.v1.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/channels")
public class ChannelController {

    private final ChannelService channelService;
    private final ChannelCreationService channelCreationService;

    @PostMapping("/hub")
    public ResponseEntity<ChannelDTO> createHubChannel(
            @Valid @RequestBody CreateHubChannelDTO createChannelDTO,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(channelCreationService.createHubChannel(createChannelDTO, user));
    }

    @PostMapping("/direct")
    public ResponseEntity<ChannelDTO> createDirectChannel(
            @Valid @RequestBody CreateDirectChannelDTO createChannelDTO,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(channelCreationService.createDirectChannel(createChannelDTO, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChannelDTO> updateChannel(
            @PathVariable Long id,
            @Valid @RequestBody UpdateChannelDTO updateChannelDTO,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(channelService.update(id, updateChannelDTO, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChannel(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        channelService.delete(id, user);
        return ResponseEntity.noContent().build();
    }
}

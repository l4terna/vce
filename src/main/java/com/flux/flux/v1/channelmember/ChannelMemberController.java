package com.flux.flux.v1.channelmember;

import com.flux.flux.v1.channelmember.dto.ChannelMemberDTO;
import com.flux.flux.v1.channelmember.dto.CreateChannelMemberDTO;
import com.flux.flux.v1.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/channels/{channelId}/members")
@RequiredArgsConstructor
public class ChannelMemberController {

    private final ChannelMemberService channelMemberService;

    @PostMapping
    public ResponseEntity<ChannelMemberDTO> createChannelMember(
        @Valid @RequestBody CreateChannelMemberDTO createMemberDTO,
        @PathVariable Long channelId
    ) {
        channelMemberService.create(channelId, createMemberDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteChannelMember(
            @PathVariable Long channelId,
            @PathVariable Long memberId,
            @AuthenticationPrincipal User user) {
        channelMemberService.delete(channelId, memberId, user);
        return ResponseEntity.noContent().build();
    }
}

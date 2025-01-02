package com.vce.vce.invite;

import com.vce.vce._shared.model.dto.PageableDTO;
import com.vce.vce.invite.dto.AcceptInviteDTO;
import com.vce.vce.invite.dto.CreateInviteDTO;
import com.vce.vce.invite.dto.InviteDTO;
import com.vce.vce.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hubs/{hubId}/invites")
@RequiredArgsConstructor
public class InviteController {
    private final InviteService inviteService;

    @GetMapping
    public ResponseEntity<Page<InviteDTO>> getAllInvites(
            @PathVariable Long hubId,
            PageableDTO pageableDTO
    ) {
        return ResponseEntity.ok(inviteService.getAllInvites(hubId, pageableDTO));
    }

    @PostMapping
    public ResponseEntity<InviteDTO> createInvite(
            @PathVariable Long hubId,
            @RequestBody CreateInviteDTO createInviteDTO,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(inviteService.create(hubId, createInviteDTO, user));
    }

    @PostMapping("/accept")
    public ResponseEntity<Void> acceptInvite(
            @PathVariable Long hubId,
            @RequestBody AcceptInviteDTO acceptInviteDTO,
            @AuthenticationPrincipal User user
    ) {
        inviteService.accept(hubId, acceptInviteDTO, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{inviteId}")
    public ResponseEntity<Void> deleteInvite(
            @PathVariable Long hubId,
            @PathVariable Long inviteId,
            @AuthenticationPrincipal User user
    ) {
        inviteService.delete(hubId, inviteId, user);
        return ResponseEntity.noContent().build();
    }
}

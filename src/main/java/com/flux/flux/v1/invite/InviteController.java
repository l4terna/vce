package com.flux.flux.v1.invite;

import com.flux.flux.v1._shared.model.dto.PageableDTO;
import com.flux.flux.v1.invite.dto.AcceptInviteDTO;
import com.flux.flux.v1.invite.dto.CreateInviteDTO;
import com.flux.flux.v1.invite.dto.InviteDTO;
import com.flux.flux.v1.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/hubs/{hubId}/invites")
@RequiredArgsConstructor
public class InviteController {
    private final InviteService inviteService;

    @GetMapping
    public ResponseEntity<Page<InviteDTO>> getAllInvites(
            @PathVariable Long hubId,
            @Valid @ModelAttribute PageableDTO pageableDTO
    ) {
        return ResponseEntity.ok(inviteService.getAllInvites(hubId, pageableDTO));
    }

    @PostMapping
    public ResponseEntity<InviteDTO> createInvite(
            @PathVariable Long hubId,
            @Valid @RequestBody CreateInviteDTO createInviteDTO,
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

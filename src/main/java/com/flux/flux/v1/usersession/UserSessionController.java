package com.flux.flux.v1.usersession;

import com.flux.flux.v1._shared.model.dto.PageableDTO;
import com.flux.flux.v1.user.User;
import com.flux.flux.v1.usersession.dto.UserSessionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-sessions")
@RequiredArgsConstructor
public class UserSessionController {
    private final UserSessionService userSessionService;

    @GetMapping
    public ResponseEntity<Page<UserSessionDTO>> getUserSessions(
            @ModelAttribute PageableDTO pageableDTO,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(userSessionService.getUserSessions(pageableDTO, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> revokeUserSession(@PathVariable Long id) {
        userSessionService.deactivateSession(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> revokeAllOtherUserSessions(@AuthenticationPrincipal User user) {
        userSessionService.deactivateAllOtherUserSessions(user);
        return ResponseEntity.noContent().build();
    }
}

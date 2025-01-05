package com.vce.vce.usersession;

import com.vce.vce._shared.model.dto.PageableDTO;
import com.vce.vce.user.User;
import com.vce.vce.usersession.dto.UserSessionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-sessions")
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
    public ResponseEntity<Void> revokeUserSession(
            @PathVariable Long id,
            @CookieValue("__fprid") String fingerprint,
            @AuthenticationPrincipal User user
    ) {
        userSessionService.deactivateSession(id, fingerprint, user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> revokeAllOtherUserSessions(@AuthenticationPrincipal User user) {
        userSessionService.deactivateAllOtherUserSessions(user);
        return ResponseEntity.noContent().build();
    }
}

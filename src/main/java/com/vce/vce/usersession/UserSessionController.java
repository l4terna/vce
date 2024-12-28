package com.vce.vce.usersession;

import com.vce.vce._shared.model.dto.PageableDTO;
import com.vce.vce.usersession.dto.UserSessionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-sessions")
@RequiredArgsConstructor
public class UserSessionController {
    private final UserSessionService userSessionService;

    @GetMapping
    public ResponseEntity<Page<UserSessionDTO>> getUserSessions(@ModelAttribute PageableDTO pageableDTO) {
        return ResponseEntity.ok(userSessionService.getUserSessions(pageableDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> revokeUserSession(@PathVariable Long id) {
        userSessionService.deactivateSession(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> revokeAllOtherUserSessions() {
        userSessionService.deactivateAllOtherUserSessions();
        return ResponseEntity.noContent().build();
    }
}

package com.vce.vce.hubs;

import com.vce.vce._shared.model.dto.PageableDTO;
import com.vce.vce.hubs.dto.CreateOrUpdateHubDTO;
import com.vce.vce.hubs.dto.HubDTO;
import com.vce.vce.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hubs")
@RequiredArgsConstructor
public class HubController {
    private final HubService hubService;
    private final HubCreationService hubCreationService;

    @GetMapping
    public ResponseEntity<Page<HubDTO>> getAllHubs(@ModelAttribute PageableDTO pageableDTO) {
        return ResponseEntity.ok(hubService.getAllHubs(pageableDTO));
    }

    @PostMapping
    public ResponseEntity<HubDTO> create(
            @RequestBody CreateOrUpdateHubDTO createHubDTO,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(hubCreationService.create(createHubDTO, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HubDTO> update(@RequestBody CreateOrUpdateHubDTO updateHubDTO, @PathVariable Long id) {
        return ResponseEntity.ok(hubService.update(id, updateHubDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HubDTO> getHubById(@PathVariable Long id) {
        return ResponseEntity.ok(hubService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        hubService.delete(id, user);
        return ResponseEntity.noContent().build();
    }
}

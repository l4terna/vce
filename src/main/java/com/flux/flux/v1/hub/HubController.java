package com.flux.flux.v1.hub;

import com.flux.flux.v1._shared.model.dto.PageableDTO;
import com.flux.flux.v1.channel.dto.HubEntitiesDTO;
import com.flux.flux.v1.hub.dto.CreateHubDTO;
import com.flux.flux.v1.hub.dto.HubDTO;
import com.flux.flux.v1.hub.dto.UpdateHubDTO;
import com.flux.flux.v1.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/hubs")
@RequiredArgsConstructor
public class HubController {
    private final HubService hubService;
    private final HubEntitiesService hubEntitiesService;
    private final HubCreationService hubCreationService;

    @GetMapping
    public ResponseEntity<Page<HubDTO>> getAllHubs(@Valid @ModelAttribute PageableDTO pageableDTO) {
        return ResponseEntity.ok(hubService.getAllHubs(pageableDTO));
    }

    @GetMapping("/@me")
    public ResponseEntity<Page<HubDTO>> getAllUserHubs(
            @Valid @ModelAttribute PageableDTO pageableDTO,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(hubService.getAllUserHubs(pageableDTO, user));
    }

    @PostMapping
    public ResponseEntity<HubDTO> create(
            @Valid @RequestBody CreateHubDTO createHubDTO,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(hubCreationService.create(createHubDTO, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HubDTO> update(
            @RequestBody UpdateHubDTO updateHubDTO,
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(hubService.update(id, updateHubDTO, user));
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


    @GetMapping("/{id}/entities")
    public ResponseEntity<HubEntitiesDTO> getHubEntities(@PathVariable Long id) {
        return ResponseEntity.ok(hubEntitiesService.getHubEntities(id));
    }
}

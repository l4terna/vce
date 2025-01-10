package com.flux.flux.v1.role;

import com.flux.flux.v1._shared.model.dto.PageableDTO;
import com.flux.flux.v1.role.dto.CreateRoleDTO;
import com.flux.flux.v1.role.dto.RoleDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/hubs/{hubId}/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleCreationService roleCreationService;
    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<Set<RoleDTO>> getRoles(
            @PathVariable Long hubId,
            @ModelAttribute PageableDTO pageableDTO
    ) {
        return ResponseEntity.ok(roleService.getHubRoles(hubId, pageableDTO));
    }

    @PostMapping
    public ResponseEntity<RoleDTO> createRole(
            @PathVariable("hubId") Long hubId,
            @Valid @RequestBody CreateRoleDTO createRoleDTO
    ) {
        return ResponseEntity.ok(roleCreationService.create(hubId, createRoleDTO));
    }
}

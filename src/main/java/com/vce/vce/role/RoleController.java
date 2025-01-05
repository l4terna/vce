package com.vce.vce.role;

import com.vce.vce._shared.model.dto.PageableDTO;
import com.vce.vce.role.dto.CreateRoleDTO;
import com.vce.vce.role.dto.RoleDTO;
import com.vce.vce.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/hubs/{hubId}/roles")
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

package com.flux.flux.v1.permission;

import com.flux.flux.v1.hubmember.HubMember;
import com.flux.flux.v1.hubmember.HubMemberService;
import com.flux.flux.v1.permission.enumeration.Permission;
import com.flux.flux.v1.role.RoleService;
import com.flux.flux.v1.role.dto.RoleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.BitSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final RoleService roleService;
    private final HubMemberService hubMemberService;

    @Transactional(readOnly = true)
    public boolean hasPermissions(Long userId, Long hubId, Permission... permissions) {
        HubMember hubMember = hubMemberService.findMemberByHubIdAndUserId(hubId, userId);

        if (hubMember.getHub().getOwner().getId().equals(userId)) {
            return true;
        }

        Set<RoleDTO> roles = roleService.findAllMemberRoles(hubMember.getId());
        BitSet totalPermissions = new BitSet();

        roles.stream()
                .map(RoleDTO::permissions)
                .map(Permission::fromBase62)
                .forEach(totalPermissions::or);

        return Permission.hasAll(totalPermissions, permissions);
    }

    @Transactional(readOnly = true)
    public void hasPermissionsThrow(Long userId, Long hubId, Permission... permissions) {
        if (!hasPermissions(userId, hubId, permissions)) {
            throw new AccessDeniedException("Permission denied");
        }
    }
}
package com.vce.vce.permission;

import com.vce.vce.member.Member;
import com.vce.vce.member.MemberService;
import com.vce.vce.permission.enumeration.Permission;
import com.vce.vce.role.RoleService;
import com.vce.vce.role.dto.RoleDTO;
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
    private final MemberService memberService;

    @Transactional(readOnly = true)
    public boolean hasPermissions(Long userId, Long hubId, Permission... permissions) {
        Member member = memberService.findByHubIdAndUserId(hubId, userId);

        if (member.getHub().getOwner().getId().equals(userId)) {
            return true;
        }

        Set<RoleDTO> roles = roleService.findAllMemberRoles(member.getId());
        BitSet totalPermissions = new BitSet();

        roles.stream()
                .map(RoleDTO::permissions)
                .map(Permission::fromString)
                .forEach(totalPermissions::or);

        return Permission.hasAll(totalPermissions, permissions);
    }

    @Transactional(readOnly = true)
    public boolean hasPermissionsThrow(Long userId, Long hubId, Permission... permissions) {
        if (!hasPermissions(userId, hubId, permissions)) {
            throw new AccessDeniedException("Insufficient permissions");
        }
        return true;
    }
}
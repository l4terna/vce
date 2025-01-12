package com.flux.flux.v1.invite;

import com.flux.flux.v1._shared.model.dto.PageableDTO;
import com.flux.flux.v1.hubs.Hub;
import com.flux.flux.v1.hubs.HubService;
import com.flux.flux.v1.invite.dto.AcceptInviteDTO;
import com.flux.flux.v1.invite.dto.CreateInviteDTO;
import com.flux.flux.v1.invite.dto.InviteDTO;
import com.flux.flux.v1.hubmember.HubMemberCreationService;
import com.flux.flux.v1.permission.PermissionService;
import com.flux.flux.v1.permission.enumeration.Permission;
import com.flux.flux.v1.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InviteService {
    private final InviteRepository inviteRepository;
    private final HubService hubService;
    private final InviteMapper inviteMapper;
    private final SecureRandom secureRandom;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqstuvwxyz0123456789";
    private static final int CODE_LENGTH = 10;
    private final HubMemberCreationService hubMemberCreationService;
    private final PermissionService permissionService;

    @Transactional
    public InviteDTO create(Long hubId, CreateInviteDTO createInviteDTO, User currentUser) {
        Hub hub = hubService.findHubById(hubId);

        permissionService.hasPermissionsThrow(currentUser.getId(), hubId, Permission.CREATE_INVITE);

        LocalDateTime expiresAt = createInviteDTO.expiresAt();

        if (expiresAt == null) {
            expiresAt = LocalDateTime.now().plusDays(7);
        }

        Invite invite = Invite.builder()
                .hub(hub)
                .code(generateCode())
                .maxUses(createInviteDTO.maxUses())
                .expiresAt(expiresAt)
                .createdBy(currentUser)
                .build();

        return inviteMapper.toDTO(inviteRepository.save(invite));
    }

    private String generateCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(secureRandom.nextInt(CHARACTERS.length())));
        }
        return code.toString();

    }

    @Transactional
    public void accept(Long hubId, AcceptInviteDTO acceptInviteDTO, User user) {
        Invite invite = inviteRepository.findByCode(acceptInviteDTO.code())
                .orElseThrow(() -> new EntityNotFoundException("Invite not found"));

        // maxUses = null = endless uses
        if (!invite.getIsActive() ||
                (invite.getMaxUses() != null && invite.getMaxUses() <= invite.getCurrentUses()) ||
                invite.getExpiresAt().isBefore(LocalDateTime.now())
        ) {
            throw new AccessDeniedException("Code expired");
        }

        hubMemberCreationService.create(hubId, user);

        invite.setCurrentUses(invite.getCurrentUses() + 1);
        inviteRepository.save(invite);
    }

    @Transactional(readOnly = true)
    public Page<InviteDTO> getAllInvites(Long hubId, PageableDTO pageableDTO) {
        return inviteRepository.findAllByHubId(hubId, pageableDTO.toPageable())
                .map(inviteMapper::toDTO);
    }

    @Transactional
    public void delete(Long hubId, Long inviteId, User currentUser) {
        Hub hub = hubService.findHubById(hubId);
        if (!hub.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Permission denied");
        }

        inviteRepository.deleteById(inviteId);
    }
}

package com.vce.vce.invite;

import com.vce.vce._shared.model.dto.PageableDTO;
import com.vce.vce.hubs.Hub;
import com.vce.vce.hubs.HubService;
import com.vce.vce.invite.dto.AcceptInviteDTO;
import com.vce.vce.invite.dto.CreateInviteDTO;
import com.vce.vce.invite.dto.InviteDTO;
import com.vce.vce.member.MemberService;
import com.vce.vce.user.User;
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
    private final MemberService memberService;

    @Transactional
    public InviteDTO create(Long hubId, CreateInviteDTO createInviteDTO, User currentUser) {
        Hub hub = hubService.findHubById(hubId);

        if (!hub.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Insufficient rights");
        }

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

        memberService.createMember(hubId, user);

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
            throw new AccessDeniedException("Insufficient rights");
        }

        inviteRepository.deleteById(inviteId);
    }
}

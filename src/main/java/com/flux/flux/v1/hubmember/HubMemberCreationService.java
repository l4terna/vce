package com.flux.flux.v1.hubmember;

import com.flux.flux.v1._shared.exception.EntityAlreadyExistsException;
import com.flux.flux.v1.hubmember.dto.HubMemberDTO;
import com.flux.flux.v1.hubs.Hub;
import com.flux.flux.v1.hubs.HubService;
import com.flux.flux.v1.hubs.enumeration.HubType;
import com.flux.flux.v1.permission.PermissionService;
import com.flux.flux.v1.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HubMemberCreationService {
    private final HubService hubService;
    private final HubMemberRepository hubMemberRepository;
    private final HubMemberMapper hubMemberMapper;
    private final PermissionService permissionService;

    @Transactional
    public HubMember create(Hub hub, User user) {
        hubMemberRepository.findByHubAndUser(hub, user)
                .ifPresent((member) -> {
                    throw new EntityAlreadyExistsException("Member already exists");
                });

        HubMember hubMember = HubMember.builder()
                .hub(hub)
                .user(user)
                .build();

        return hubMemberRepository.save(hubMember);
    }

    @Transactional
    public HubMemberDTO create(Long hubId, User user) {
        Hub hub = hubService.findHubById(hubId);

        if (hub.getType() != HubType.PUBLIC) {
            throw new AccessDeniedException("Permission denied");
        }

        return hubMemberMapper.toDTO(create(hub, user));
    }
}

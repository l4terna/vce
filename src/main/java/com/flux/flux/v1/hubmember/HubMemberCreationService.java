package com.flux.flux.v1.hubmember;

import com.flux.flux.v1._shared.exception.EntityAlreadyExistsException;
import com.flux.flux.v1.hubmember.dto.HubMemberDTO;
import com.flux.flux.v1.hubs.Hub;
import com.flux.flux.v1.hubs.HubService;
import com.flux.flux.v1.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HubMemberCreationService {
    private final HubService hubService;
    private final HubMemberRepository hubMemberRepository;
    private final HubMemberMapper hubMemberMapper;

    @Transactional
    public HubMemberDTO createMember(Hub hub, User user) {
        hubMemberRepository.findByHubAndUser(hub, user)
                .ifPresent((member) -> {
                    throw new EntityAlreadyExistsException("Member already exists");
                });

        HubMember hubMember = HubMember.builder()
                .hub(hub)
                .user(user)
                .build();

        return hubMemberMapper.toDTO(hubMemberRepository.save(hubMember));
    }

    @Transactional
    public HubMemberDTO createMember(Long hubId, User user) {
        Hub hub = hubService.findHubById(hubId);

        return createMember(hub, user);
    }
}

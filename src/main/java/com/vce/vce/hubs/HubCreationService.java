package com.vce.vce.hubs;

import com.vce.vce.hubs.dto.CreateHubDTO;
import com.vce.vce.hubs.dto.HubDTO;
import com.vce.vce.member.MemberCreationService;
import com.vce.vce.member.MemberService;
import com.vce.vce.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HubCreationService {
    private final HubMapper hubMapper;
    private final HubRepository hubRepository;
    private final MemberService memberService;
    private final MemberCreationService memberCreationService;

    @Transactional
    public HubDTO create(CreateHubDTO createHubDTO, User currentUser) {
        Hub hub = Hub.builder()
                .name(createHubDTO.name())
                .owner(currentUser)
                .build();

        Hub savedHub = hubRepository.save(hub);

        memberCreationService.createMember(savedHub, currentUser);

        return hubMapper.toDTO(savedHub);
    }
}

package com.flux.flux.v1.hub;

import com.flux.flux.v1.hub.dto.CreateHubDTO;
import com.flux.flux.v1.hub.dto.HubDTO;
import com.flux.flux.v1.hubmember.HubMemberService;
import com.flux.flux.v1.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HubCreationService {
    private final HubRepository hubRepository;
    private final HubMemberService hubMemberService;
    private final HubMapper hubMapper;

    @Transactional
    public HubDTO create(CreateHubDTO createHubDTO, User currentUser) {
        Hub hub = Hub.builder()
                .name(createHubDTO.name())
                .owner(currentUser)
                .type(createHubDTO.type())
                .build();

        Hub savedHub = hubRepository.save(hub);

        hubMemberService.createHubMember(savedHub, currentUser);

        return hubMapper.toDTO(savedHub);
    }
}

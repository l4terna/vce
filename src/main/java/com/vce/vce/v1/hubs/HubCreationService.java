package com.vce.vce.v1.hubs;

import com.vce.vce.v1.hubs.dto.CreateHubDTO;
import com.vce.vce.v1.hubs.dto.HubDTO;
import com.vce.vce.v1.hubmember.HubMemberCreationService;
import com.vce.vce.v1.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HubCreationService {
    private final HubMapper hubMapper;
    private final HubRepository hubRepository;
    private final HubMemberCreationService hubMemberCreationService;

    @Transactional
    public HubDTO create(CreateHubDTO createHubDTO, User currentUser) {
        Hub hub = Hub.builder()
                .name(createHubDTO.name())
                .owner(currentUser)
                .build();

        Hub savedHub = hubRepository.save(hub);

        hubMemberCreationService.createMember(savedHub, currentUser);

        return hubMapper.toDTO(savedHub);
    }
}

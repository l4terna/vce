package com.flux.flux.v1.hub;

import com.flux.flux.v1.hub.dto.CreateHubDTO;
import com.flux.flux.v1.hub.dto.HubDTO;
import com.flux.flux.v1.hub.enumeration.HubType;
import com.flux.flux.v1.hubavatar.HubAvatarService;
import com.flux.flux.v1.hubmember.HubMemberService;
import com.flux.flux.v1.media.LocalMediaStorageService;
import com.flux.flux.v1.media.enumeration.MediaVisibility;
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
    private final LocalMediaStorageService localMediaStorageService;
    private final HubAvatarService hubAvatarService;

    @Transactional
    public HubDTO create(CreateHubDTO createHubDTO, User currentUser) {
        HubType type = HubType.fromValue(createHubDTO.type());

        Hub hub = Hub.builder()
                .name(createHubDTO.name())
                .owner(currentUser)
                .type(type)
                .build();

        Hub savedHub = hubRepository.save(hub);

        if (createHubDTO.avatar() != null && !createHubDTO.avatar().isEmpty()) {
            hubAvatarService.save(createHubDTO.avatar(), savedHub.getId(), currentUser.getId());
        }

        hubMemberService.createHubMember(savedHub, currentUser);

        return hubMapper.toDTO(savedHub);
    }
}

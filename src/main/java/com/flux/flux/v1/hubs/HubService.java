package com.flux.flux.v1.hubs;

import com.flux.flux.v1._shared.model.dto.PageableDTO;
import com.flux.flux.v1.hubs.dto.HubDTO;
import com.flux.flux.v1.hubs.dto.UpdateHubDTO;
import com.flux.flux.v1.permission.PermissionService;
import com.flux.flux.v1.permission.enumeration.Permission;
import com.flux.flux.v1.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class HubService {
    private final HubMapper hubMapper;
    private final HubRepository hubRepository;
    private final PermissionService permissionService;

    @Transactional(readOnly = true)
    public Page<HubDTO> getAllHubs(PageableDTO pageableDTO) {
        return hubRepository.findAll(pageableDTO.toPageable())
                .map(hubMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<HubDTO> getAllUserHubs(PageableDTO pageableDTO, User currentUser) {
        return hubRepository.findAllByUserId(pageableDTO.toPageable(), currentUser.getId())
                .map(hubMapper::toDTO);
    }

    @Transactional
    public HubDTO update(Long id, UpdateHubDTO updateHubDTO, User currentUser) {
        Hub hub = hubRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Hub not found"));

        permissionService.hasPermissionsThrow(currentUser.getId(), id, Permission.MANAGE_HUB);

        if (updateHubDTO.name() != null && !updateHubDTO.name().equals(hub.getName())) {
            hub.setName(updateHubDTO.name());
        }

        if (updateHubDTO.type() != hub.getType()) {
            hub.setType(updateHubDTO.type());
        }

        hubRepository.save(hub);

        return hubMapper.toDTO(hub);
    }

    @Transactional(readOnly = true)
    public HubDTO findById(Long id) {
        return hubRepository.findById(id)
                .map(hubMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Hub not found"));
    }

    @Transactional(readOnly = true)
    public Hub findHubById(Long id) {
        return hubRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hub not found"));
    }

    @Transactional
    public void delete(Long id, User currentUser) {
        Hub hub = findHubById(id);

        if (!hub.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Permission denied");
        }

        hubRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Hub findHubByChannelId(Long channelId) {
        return hubRepository.findHubByChannelId(channelId)
                .orElseThrow(() -> new EntityNotFoundException("Hub not found by channel id: " + channelId));
    }
}

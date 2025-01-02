package com.vce.vce.hubs;

import com.vce.vce._shared.model.dto.PageableDTO;
import com.vce.vce.hubs.dto.CreateOrUpdateHubDTO;
import com.vce.vce.hubs.dto.HubDTO;
import com.vce.vce.user.User;
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

    @Transactional(readOnly = true)
    public Page<HubDTO> getAllHubs(PageableDTO pageableDTO) {
        return hubRepository.findAll(pageableDTO.toPageable())
                .map(hubMapper::toDTO);
    }

    @Transactional
    public HubDTO update(Long id, CreateOrUpdateHubDTO updateHubDTO) {
        Hub hub = hubRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Hub not found"));

        hub.setName(updateHubDTO.name());
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
            throw new AccessDeniedException("Insufficient rights");
        }

        hubRepository.deleteById(id);
    }
}

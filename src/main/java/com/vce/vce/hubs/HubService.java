package com.vce.vce.hubs;

import com.vce.vce._shared.model.dto.PageableDTO;
import com.vce.vce._shared.security.UserContextHolder;
import com.vce.vce.hubs.dto.CreateOrUpdateHubDTO;
import com.vce.vce.hubs.dto.HubDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public Hub createHub(CreateOrUpdateHubDTO createHubDTO) {
        Hub hub = Hub.builder()
                .name(createHubDTO.name())
                .owner(UserContextHolder.getCurrentUser())
                .build();

        return hubRepository.save(hub);
    }

    @Transactional
    public HubDTO create(CreateOrUpdateHubDTO createHubDTO) {
        return hubMapper.toDTO(createHub(createHubDTO));
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

    @Transactional
    public void delete(Long id) {
        hubRepository.deleteById(id);
    }
}

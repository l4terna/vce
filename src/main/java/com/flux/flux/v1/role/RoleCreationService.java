package com.flux.flux.v1.role;

import com.flux.flux.v1._shared.exception.EntityAlreadyExistsException;
import com.flux.flux.v1.hubs.Hub;
import com.flux.flux.v1.hubs.HubService;
import com.flux.flux.v1.role.dto.CreateRoleDTO;
import com.flux.flux.v1.role.dto.RoleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleCreationService {
    private final RoleRepository roleRepository;
    private final HubService hubService;
    private final RoleMapper roleMapper;

    @Transactional
    public RoleDTO create(Long hubId, CreateRoleDTO createRoleDTO) {
        Hub hub = hubService.findHubById(hubId);

        if (roleRepository.existsByHubIdAndName(hubId, createRoleDTO.name())) {
            throw new EntityAlreadyExistsException("Role with name " + createRoleDTO.name() + " already exists1");
        }

        Role role = Role.builder()
                .name(createRoleDTO.name())
                .hub(hub)
                .color(createRoleDTO.color())
                .permissions(createRoleDTO.permissions())
                .build();

        return roleMapper.toDTO(roleRepository.save(role));
    }
}

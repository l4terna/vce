package com.flux.flux.v1.channel;

import com.flux.flux.v1.category.Category;
import com.flux.flux.v1.category.CategoryService;
import com.flux.flux.v1.channel.dto.ChannelDTO;
import com.flux.flux.v1.channel.dto.CreateHubChannelDTO;
import com.flux.flux.v1.channel.dto.CreateDirectChannelDTO;
import com.flux.flux.v1.channel.enumeration.ChannelType;
import com.flux.flux.v1.channelmember.ChannelMemberService;
import com.flux.flux.v1.channelmember.dto.CreateChannelMemberDTO;
import com.flux.flux.v1.permission.PermissionService;
import com.flux.flux.v1.permission.enumeration.Permission;
import com.flux.flux.v1.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelCreationService {
    private final ChannelRepository channelRepository;
    private final CategoryService categoryService;
    private final PermissionService permissionService;
    private final ChannelMapper channelMapper;
    private final ChannelService channelService;
    private final ChannelMemberService channelMemberService;

    @Transactional
    public ChannelDTO createHubChannel(CreateHubChannelDTO createChannelDTO, User currentUser) {
        Category category = categoryService.findCategoryByIdWithHub(createChannelDTO.categoryId());

        if (createChannelDTO.type() != ChannelType.TEXT && createChannelDTO.type() != ChannelType.VOICE) {
            throw new IllegalArgumentException("Channel type must be either TEXT or VOICE");
        }

        permissionService.hasPermissionsThrow(currentUser.getId(), category.getId(), Permission.MANAGE_CHANNELS);

        Channel channel = Channel.builder()
                .categoryId(category.getId())
                .type(createChannelDTO.type())
                .owner(currentUser)
                .name(createChannelDTO.name())
                .position(channelService.getLastPosition(category.getId()))
                .build();

        return channelMapper.toDTO(channelRepository.save(channel));
    }

    @Transactional
    public ChannelDTO createDirectChannel(CreateDirectChannelDTO createChannelDTO, User currentUser) {
        Channel channel;

        if (createChannelDTO.members().size() == 1 &&
                !createChannelDTO.members().get(0).equals(currentUser.getId())
        ) {
            channel = channelRepository.findDirectChannelByMemberIds(currentUser.getId(), createChannelDTO.members().get(0))
                    .orElseGet(() -> createDirectOrGroupChannel(createChannelDTO.members(), ChannelType.DC));
        } else {
            createChannelDTO.members().add(currentUser.getId());
            channel = createDirectOrGroupChannel(createChannelDTO.members(), ChannelType.GROUP_DC);
        }

        return channelMapper.toDTO(channelRepository.save(channel));
    }

    private Channel createDirectOrGroupChannel(List<Long> memberIds, ChannelType type) {
        if (type != ChannelType.GROUP_DC && type != ChannelType.DC) {
            throw new IllegalArgumentException("Channel type must be either DC or GROUP_DC");
        }

        Channel channel = Channel.builder()
                .type(type)
                .build();

        Channel newChan = channelRepository.save(channel);

        CreateChannelMemberDTO createChanMembers = CreateChannelMemberDTO.builder()
                .users(memberIds)
                .build();

        channelMemberService.create(newChan.getId(), createChanMembers);

        return newChan;
    }
}

package com.flux.flux.v1.channelmember;

import com.flux.flux.v1._shared.model.dto.PageableDTO;
import com.flux.flux.v1.channel.Channel;
import com.flux.flux.v1.channel.ChannelService;
import com.flux.flux.v1.channel.enumeration.ChannelType;
import com.flux.flux.v1.channelmember.dto.ChannelMemberDTO;
import com.flux.flux.v1.channelmember.dto.CreateChannelMemberDTO;
import com.flux.flux.v1.user.User;
import com.flux.flux.v1.user.UserService;
import com.flux.flux.v1.userpresence.tracking.ChannelTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChannelMemberService {

    private final ChannelService channelService;
    private final UserService userService;
    private final ChannelMemberRepository channelMemberRepository;
    private final ChannelTrackingService channelTrackingService;
    private final ChannelMemberMapper channelMemberMapper;

    public Page<ChannelMemberDTO> getChannelMembers(Long channelId, PageableDTO pageableDTO) {
        Channel channel = channelService.findChannelById(channelId);

        if (channel.getType() == ChannelType.DC || channel.getType() == ChannelType.GROUP_DC) {
            Pageable pageable = pageableDTO.toPageable();

            Set<Long> userIds = channelTrackingService.getAllOnlineUserIds(channelId);

            return channelMemberRepository.findAllByChannelIdAndSortByUserIds(channelId, userIds, pageable)
                    .map(channelMemberMapper::toDTO);
        } else if (channel.getType() == ChannelType.TEXT) {
//            channelMemberRepository.findAllByChannelIdAndSortByUserIdsWithPermissions(channelId, );
        }

        return null;
    }

    @Transactional
    public void create(Long channelId, CreateChannelMemberDTO createMemberDTO) {
        Channel channel = channelService.findChannelById(channelId);
        Set<Long> existingUserIds = channelMemberRepository.findChannelUserIds(channel.getId());

        List<User> users = userService.findAllByIds(
                createMemberDTO.users().stream()
                        .filter(userId -> !existingUserIds.contains(userId))
                        .collect(Collectors.toList())
        );

        List<ChannelMember> members = users.stream()
                .map(user -> ChannelMember.builder()
                        .channel(channel)
                        .user(user)
                        .build())
                .collect(Collectors.toList());

        channelMemberRepository.saveAll(members);
    }

    @Transactional
    public void delete(Long channelId, Long memberId, User currentUser) {
        Channel channel = channelService.findChannelById(channelId);

        if (!channel.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Permission denied");
        }

        channelMemberRepository.deleteById(memberId);
    }

    @Transactional(readOnly = true)
    public boolean isMember(Long channelId, Long userId) {
        return channelMemberRepository.existsByIdAndUserId(channelId, userId);
    }

    @Transactional(readOnly = true)
    public void isMemberThrow(Long channelId, Long userId) {
        if (!isMember(channelId, userId)) {
            throw new AccessDeniedException("Permissions denied");
        }
    }
}

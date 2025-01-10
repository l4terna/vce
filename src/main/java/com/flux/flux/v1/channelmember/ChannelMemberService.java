package com.flux.flux.v1.channelmember;

import com.flux.flux.v1.channel.Channel;
import com.flux.flux.v1.channel.ChannelService;
import com.flux.flux.v1.channelmember.dto.CreateChannelMemberDTO;
import com.flux.flux.v1.user.User;
import com.flux.flux.v1.user.UserService;
import lombok.RequiredArgsConstructor;
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
    public void delete(Long channelId, Long memberId) {
        Channel channel = channelService.findChannelById(channelId);
    }
}

package com.vce.vce.member;

import com.vce.vce._shared.exception.EntityAlreadyExistsException;
import com.vce.vce.hubs.Hub;
import com.vce.vce.hubs.HubService;
import com.vce.vce.member.dto.MemberDTO;
import com.vce.vce.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberCreationService {
    private final HubService hubService;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Transactional
    public MemberDTO createMember(Hub hub, User user) {
        memberRepository.findByHubAndUser(hub, user)
                .ifPresent((member) -> {
                    throw new EntityAlreadyExistsException("Member already exists");
                });

        Member member = Member.builder()
                .hub(hub)
                .user(user)
                .build();

        return memberMapper.toDTO(memberRepository.save(member));
    }

    @Transactional
    public MemberDTO createMember(Long hubId, User user) {
        Hub hub = hubService.findHubById(hubId);

        return createMember(hub, user);
    }
}

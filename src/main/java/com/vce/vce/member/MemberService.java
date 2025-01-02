package com.vce.vce.member;

import com.vce.vce._shared.exception.EntityAlreadyExistsException;
import com.vce.vce._shared.model.dto.PageableDTO;
import com.vce.vce.hubs.Hub;
import com.vce.vce.hubs.HubService;
import com.vce.vce.member.dto.MemberDTO;
import com.vce.vce.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final HubService hubService;

    @Transactional(readOnly = true)
    public Page<MemberDTO> getAllMembers(Long hubId, PageableDTO pageableDTO) {
        return memberRepository.findAllByHubId(hubId, pageableDTO.toPageable())
                .map(memberMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
    }

    @Transactional
    public void deleteMember(Long hubId, Long memberId, User currentUser) {
        Member member = findMemberById(memberId);

        if (member.getHub().getId().equals(hubId) || member.getHub().getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Insufficient rights");
        }

        memberRepository.delete(member);
    }

    @Transactional
    public MemberDTO createMember(Long hubId, User user) {
        Hub hub = hubService.findHubById(hubId);

        return createMember(hub, user);
    }

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
}

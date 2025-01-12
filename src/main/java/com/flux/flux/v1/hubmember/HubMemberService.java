package com.flux.flux.v1.hubmember;

import com.flux.flux.v1._shared.model.dto.PageableDTO;
import com.flux.flux.v1.hubmember.dto.HubMemberDTO;
import com.flux.flux.v1.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class HubMemberService {
    private final HubMemberRepository hubMemberRepository;
    private final HubMemberMapper hubMemberMapper;

    @Transactional(readOnly = true)
    public Page<HubMemberDTO> getAllMembers(Long hubId, PageableDTO pageableDTO) {
        return hubMemberRepository.findAllByHubId(hubId, pageableDTO.toPageable())
                .map(hubMemberMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public HubMember findMemberById(Long id) {
        return hubMemberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
    }

    @Transactional
    public void deleteMember(Long hubId, Long memberId, User currentUser) {
        HubMember hubMember = findMemberById(memberId);

        if (hubMember.getHub().getId().equals(hubId) || hubMember.getHub().getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Permission denied");
        }

        hubMemberRepository.delete(hubMember);
    }

    @Transactional(readOnly = true)
    public HubMember findMemberByHubIdAndUserId(Long hubId, Long userId) {
        return hubMemberRepository.findByUserIdAndHubId(userId, hubId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
    }

    @Transactional(readOnly = true)
    public HubMemberDTO findByHubIdAndUserId(Long hubId, Long userId) {
        return hubMemberMapper.toDTO(findMemberByHubIdAndUserId(hubId, userId));
    }
}

package com.vce.vce.v1.hubmember;

import com.vce.vce.v1.hubs.Hub;
import com.vce.vce.v1.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface HubMemberRepository extends JpaRepository<HubMember, Long> {
    Page<HubMember> findAllByHubId(Long hubId, Pageable pageable);

    Optional<HubMember> findByHubAndUser(Hub hub, User user);

    Optional<HubMember> findByUserId(Long userId);

    Optional<HubMember> findByUserIdAndHubId(Long userId, Long hubId);
}

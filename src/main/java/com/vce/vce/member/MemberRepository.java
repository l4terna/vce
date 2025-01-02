package com.vce.vce.member;

import com.vce.vce.hubs.Hub;
import com.vce.vce.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface MemberRepository extends JpaRepository<Member, Long> {
    Page<Member> findAllByHubId(Long hubId, Pageable pageable);

    Optional<Member> findByHubAndUser(Hub hub, User user);
}

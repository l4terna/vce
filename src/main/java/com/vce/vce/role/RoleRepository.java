package com.vce.vce.role;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByHubIdAndName(Long hubId, String name);

    Page<Role> findByHubId(Long hubId, Pageable pageable);

    @Query(value = "SELECT r.* FROM roles r " +
            "JOIN members_roles mr ON r.id = mr.role_id " +
            "WHERE mr.member_id = :memberId",
            nativeQuery = true)
    Set<Role> findAllMemberRoles(Long memberId);
}

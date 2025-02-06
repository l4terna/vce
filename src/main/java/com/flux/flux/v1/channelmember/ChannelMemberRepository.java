package com.flux.flux.v1.channelmember;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
interface ChannelMemberRepository extends JpaRepository<ChannelMember, Long> {
    @Query("SELECT cm.user.id FROM ChannelMember cm JOIN cm.channel c " +
            "WHERE c.id = :channelId " +
            "AND c.type = 'GROUP_DC'")
    Set<Long> findChannelUserIds(Long channelId);

    @Query("SELECT COUNT(*) > 0 FROM Channel c JOIN c.members cm " +
            "WHERE cm.user.id = :userId AND c.id = :channelId")
    boolean existsByIdAndUserId(Long channelId, Long userId);
}

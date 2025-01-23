package com.flux.flux.v1.channelmember;

import org.hibernate.annotations.BatchSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
interface ChannelMemberRepository extends JpaRepository<ChannelMember, Long> {
    @BatchSize(size = 100)
    <S extends ChannelMember> List<S> saveAll(Iterable<S> entities);

    @Query("SELECT cm.user.id FROM ChannelMember cm JOIN cm.channel c " +
            "WHERE c.id = :channelId " +
            "AND c.type = 'GROUP_DC'")
    Set<Long> findChannelUserIds(Long channelId);

    @Query("SELECT COUNT(*) > 0 FROM Channel c JOIN c.members cm " +
            "WHERE cm.user.id = :userId AND c.id = :channelId")
    boolean existsByIdAndUserId(Long channelId, Long userId);
}

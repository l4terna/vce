package com.flux.flux.v1.channel;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface ChannelRepository extends JpaRepository<Channel, Long> {
    @Query("SELECT MAX(c.position) FROM Channel c WHERE c.categoryId = :categoryId")
    Optional<Integer> findMaxPositionByCategoryId(Long categoryId);

    @Modifying
    @Query("UPDATE Channel c SET c.position = c.position + :delta " +
            "WHERE c.categoryId = :categoryId " +
            "AND c.position >= :startPosition " +
            "AND (:endPosition IS NULL OR c.position <= :endPosition)")
    void shiftPositions(Long categoryId, Integer startPosition, Integer endPosition, Integer delta);

    @Query("SELECT DISTINCT c FROM Channel c " +
            "JOIN ChannelMember cm1 ON cm1.channel = c " +
            "JOIN ChannelMember cm2 ON cm2.channel = c " +
            "WHERE c.type = 'DC' " +
            "AND cm1.user.id = :firstMemberId " +
            "AND cm2.user.id = :secondMemberId")
    Optional<Channel> findDirectChannelByMemberIds(Long firstMemberId, Long secondMemberId);

    @Query("SELECT c FROM Channel c JOIN Category ct ON ct.id = c.categoryId " +
            "WHERE ct.hub.id = :hubId")
    List<Channel> findAllByHubId(Long hubId);
}

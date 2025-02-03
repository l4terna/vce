package com.flux.flux.v1.messagestatus;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
interface MessageStatusRepository extends JpaRepository<MessageReadStatus, Long> {

    @EntityGraph(attributePaths = {"message"})
    @Query("SELECT mrs FROM MessageReadStatus mrs " +
            "WHERE mrs.message.id IN :messageIds " +
            "AND mrs.user.id != :userId")
    Set<MessageReadStatus> findByMessageIdsAndWithoutUserId(List<Long> messageIds, Long userId);

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT mrs FROM MessageReadStatus mrs " +
            "WHERE mrs.message.id = :messageId AND mrs.user.id IN :userIds")
    Set<MessageReadStatus> findByMessageIdAndUserIds(Long messageId, Set<Long> userIds);

    @Query("SELECT mrs.message.id, count(*) FROM MessageReadStatus mrs " +
            "WHERE mrs.message.id IN :messageIds " +
            "GROUP BY mrs.message.id")
    List<Long[]> countByMessageIds(List<Long> messageIds);

    long countByMessageId(Long messageId);
}

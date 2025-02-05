package com.flux.flux.v1.message;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByChannelId(Long channelId, Pageable pageable);

    @Query("SELECT m FROM Message m " +
            "WHERE m.channelId = :channelId AND m.id < :beforeId " +
            "ORDER BY m.id DESC")
    List<Message> findAllByChannelIdAndBeforeId(Long channelId, Long beforeId, Pageable pageable);

    @Query("SELECT m FROM Message m " +
            "WHERE m.channelId = :channelId AND m.id > :afterId " +
            "ORDER BY m.id ASC")
    List<Message> findAllByChannelIdAndAfterId(Long channelId, Long afterId, Pageable pageable);

    @Query("SELECT m FROM Message m " +
            "WHERE m.channelId = :channelId AND (m.id >= :aroundId OR m.id <= :aroundId) " +
            "ORDER BY m.id ASC")
    List<Message> findAllByChannelIdAndAroundId(Long channelId, Long aroundId, Pageable pageable);

    @Query("SELECT m FROM Message m WHERE m.id IN :messageIds AND m.channelId = :channelId")
    Set<Message> findMessagesByIdsAndChannelId(Set<Long> messageIds, Long channelId);
}

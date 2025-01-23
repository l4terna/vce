package com.flux.flux.v1.messageread;

import com.flux.flux.v1.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface MessageReadStatusRepository extends JpaRepository<MessageReadStatus, Long> {

    @Query("SELECT mrs FROM MessageReadStatus mrs " +
            "WHERE mrs.message.id IN :messageIds " +
            "AND mrs.user.id != :userId")
    List<MessageReadStatus> findByMessageIdsAndUserId(List<Long> messageIds, Long userId);

    @Query("SELECT mrs.message.id, count(*) FROM MessageReadStatus mrs " +
            "WHERE mrs.message.id IN :messageIds " +
            "GROUP BY mrs.message.id")
    List<Long[]> countByMessageIds(List<Long> messageIds);

    List<MessageReadStatus> message(Message message);
}

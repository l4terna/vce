package com.flux.flux.v1.messagestatus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageReadStatusService {
    private final MessageStatusRepository messageStatusRepository;

    @Transactional(readOnly = true)
    public Set<MessageReadStatus> findReadStatusesByMessageIdsAndWithoutUserId(List<Long> messageIds, Long userId) {
        return messageStatusRepository.findByMessageIdsAndWithoutUserId(messageIds, userId);
    }

    @Transactional(readOnly = true)
    public Set<Long> findReadStatusesByMessageIdAndUserIds(Long messageId, Set<Long> userIds) {
        return messageStatusRepository.findByMessageIdAndUserIds(messageId, userIds)
                .stream()
                .map(mrs -> mrs.getUser().getId())
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public Map<Long, Long> countReadStatusesByMessageIds(List<Long> messageIds) {
        return messageStatusRepository.countByMessageIds(messageIds)
                .stream()
                .collect(Collectors.toMap(
                        arr -> arr[0],
                        arr -> arr[1]
                ));
    }

    @Transactional(readOnly = true)
    public long countReadStatusesByMessageId(Long messageId) {
        return messageStatusRepository.countByMessageId(messageId);
    }
}

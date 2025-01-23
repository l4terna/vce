package com.flux.flux.v1.messageread;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageReadStatusService {
    private final MessageReadStatusRepository messageReadStatusRepository;

    @Transactional(readOnly = true)
    public List<MessageReadStatus> findReadStatusesByMessageIdsAndUserId(List<Long> messageIds, Long userId) {
        return messageReadStatusRepository.findByMessageIdsAndUserId(messageIds, userId);
    }

    @Transactional(readOnly = true)
    public Map<Long, Long> countReadStatusesByMessageIds(List<Long> messageIds) {
        return messageReadStatusRepository.countByMessageIds(messageIds)
                .stream()
                .collect(Collectors.toMap(
                        arr -> arr[0],
                        arr -> arr[1]
                ));
    }
}

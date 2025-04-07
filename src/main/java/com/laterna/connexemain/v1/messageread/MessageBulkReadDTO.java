package com.laterna.connexemain.v1.messageread;


import java.util.Set;

public record MessageBulkReadDTO(
        Set<Long> messageIds
) {
}

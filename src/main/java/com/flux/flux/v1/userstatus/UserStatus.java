package com.flux.flux.v1.userstatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "user_status", timeToLive = 30)
public class UserStatus implements Serializable {
    @Id
    private String id; // userId:fingerprint

    @Indexed
    private Long userId;

    @Indexed
    private String fingerprint;

    @Indexed
    private String webSocketSessionId;
}

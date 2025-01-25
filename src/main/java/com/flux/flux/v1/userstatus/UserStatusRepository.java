package com.flux.flux.v1.userstatus;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface UserStatusRepository extends CrudRepository<UserStatus, Long> {
    Optional<UserStatus> findByWebSocketSessionId(String webSocketId);
    boolean existsByUserId(Long userId);
}

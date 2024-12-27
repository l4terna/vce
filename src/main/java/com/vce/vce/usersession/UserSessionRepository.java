package com.vce.vce.usersession;

import com.vce.vce.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findByFingerprintAndUser(String fingerprint, User user);
}

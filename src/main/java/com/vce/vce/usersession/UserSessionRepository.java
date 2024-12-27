package com.vce.vce.usersession;

import com.vce.vce.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    @Modifying
    @Query("UPDATE UserSession us SET us.isActive = false WHERE us.fingerprint = :fingerprint AND us.user = :user")
    void deactivateSessionsByFingerprintAndUser(String fingerprint, User user);
}

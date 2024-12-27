package com.vce.vce.token.shared;

import com.vce.vce.usersession.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface TokenRepository<T extends TokenEntity> extends JpaRepository<T, Long> {
    Optional<T> findByToken(String token);

    @Modifying
    @Query("UPDATE #{#entityName} t SET t.isRevoked = true WHERE t.userSession = :userSession AND t.isRevoked = false")
    void revokeAllActiveTokensByUserSession(UserSession userSession);
}

package com.vce.vce.token.shared;

import com.vce.vce.token.refresh.RefreshToken;
import com.vce.vce.usersession.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface TokenRepository<T extends TokenEntity> extends JpaRepository<T, Long> {
    Optional<T> findByToken(String token);

    @Query("SELECT t FROM #{#entityName} t " +
            "WHERE t.userSession = :userSession " +
            "AND t.isRevoked = false")
    List<T> findAllActiveTokensByUserSession(UserSession userSession);
}

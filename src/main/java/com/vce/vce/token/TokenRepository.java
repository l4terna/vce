package com.vce.vce.token;

import com.vce.vce.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByAccessTokenAndFingerprint(String accessToken, String fingerprint);

    @Query("SELECT t FROM Token t " +
            "WHERE t.isRevoked = false " +
            "AND t.fingerprint = :fingerprint " +
            "AND t.user = :user")
    List<Token> findAllActiveTokens(User user, String fingerprint);
}

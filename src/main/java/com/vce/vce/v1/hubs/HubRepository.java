package com.vce.vce.v1.hubs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface HubRepository extends JpaRepository<Hub, Long> {

    @Query("SELECT h FROM Hub h " +
            "JOIN Category c ON c.hub.id = h.id " +
            "WHERE c.id = :category")
    Optional<Hub> findHubByCategoryId(Long categoryId);

    @Query("SELECT h FROM Hub h " +
            "JOIN Category ct ON ct.hub.id = h.id " +
            "JOIN Channel ch ON ch.categoryId = ct.id " +
            "WHERE ch.id = :channelId")
    Optional<Hub> findHubByChannelId(Long channelId);
}

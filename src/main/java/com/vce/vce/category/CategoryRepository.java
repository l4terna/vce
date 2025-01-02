package com.vce.vce.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByHubIdOrderByPosition(Long hubId);

    @Query("SELECT MAX(c.position) FROM Category c " +
            "WHERE c.hub.id = :hubId")
    Optional<Integer> findMaxPositionByHubId(Long hubId);

    @Modifying
    @Query("UPDATE Category c SET c.position = c.position + :delta " +
            "WHERE c.position >= :startPosition AND c.hub.id = :hubId " +
            "AND (:endPosition IS NULL OR c.position <= :endPosition)")
    void shiftPositions(Long hubId, Integer startPosition, Integer endPosition, Integer delta);
}

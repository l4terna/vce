package com.vce.vce.hubs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface HubRepository extends JpaRepository<Hub, Long> {
}

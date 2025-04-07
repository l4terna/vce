package com.flux.flux.v1.useravatar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface UserAvatarRepository extends JpaRepository<UserAvatar, Long> {
}

package com.vce.vce.usersession;

import com.vce.vce._shared.entity.BaseEntity;
import com.vce.vce.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_sessions")
public class UserSession extends BaseEntity {
    @Column(nullable = false)
    private LocalDateTime lastActivity;

    @Column(nullable = false)
    private String ipAddress;

    @Column(nullable = false)
    private String deviceInfo;

    @Column(nullable = false)
    private String fingerprint;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
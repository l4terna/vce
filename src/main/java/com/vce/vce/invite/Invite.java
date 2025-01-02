package com.vce.vce.invite;

import com.vce.vce._shared.model.entity.BaseEntity;
import com.vce.vce.hubs.Hub;
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
@Table(name = "invites")
public class Invite extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false, columnDefinition = "maxUses = null = endless uses")
    private Integer maxUses;

    @Builder.Default
    @Column(nullable = false)
    private Integer currentUses = 0;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @ManyToOne
    @JoinColumn(name = "hub_id")
    private Hub hub;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;
}

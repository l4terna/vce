package com.flux.flux.v1.hubmember;

import com.flux.flux.v1._shared.model.entity.IdEntity;
import com.flux.flux.v1.hubs.Hub;
import com.flux.flux.v1.user.User;
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
@Table(name = "hub_members")
public class HubMember extends IdEntity {
    @ManyToOne
    @JoinColumn(name = "hub_id")
    private Hub hub;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    @Column(name = "joined_at")
    private LocalDateTime joinedAt = LocalDateTime.now();
}

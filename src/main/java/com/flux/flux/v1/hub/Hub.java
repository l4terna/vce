package com.flux.flux.v1.hub;

import com.flux.flux.v1._shared.model.entity.BaseEntity;
import com.flux.flux.v1.hub.enumeration.HubType;
import com.flux.flux.v1.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hubs")
public class Hub extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private HubType type;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}

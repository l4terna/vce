package com.vce.vce.token.shared;

import com.vce.vce._shared.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class TokenEntity extends BaseEntity {
    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isRevoked = false;

    @Column(nullable = false)
    private LocalDateTime expiresAt;


}

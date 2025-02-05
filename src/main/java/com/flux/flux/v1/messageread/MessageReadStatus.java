package com.flux.flux.v1.messageread;

import com.flux.flux.v1._shared.model.entity.IdEntity;
import com.flux.flux.v1.message.Message;
import com.flux.flux.v1.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "message_read_statuses",
        uniqueConstraints = @UniqueConstraint(columnNames = {"message_id", "user_id"})
)
public class MessageReadStatus extends IdEntity {
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long messageId;

    @Builder.Default
    private Instant readAt = Instant.now();
}
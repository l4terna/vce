package com.vce.vce.v1.channelmember;


import com.vce.vce.v1._shared.model.entity.IdEntity;
import com.vce.vce.v1.channel.Channel;
import com.vce.vce.v1.user.User;
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
@Table(name = "channel_members")
public class ChannelMember extends IdEntity {
    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    @Column(name = "joined_at")
    private LocalDateTime joinedAt = LocalDateTime.now();
}

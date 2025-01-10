package com.flux.flux.v1.message;

import com.flux.flux.v1._shared.model.entity.BaseEntity;
import com.flux.flux.v1.channel.Channel;
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
@Table(name = "messages")
public class Message extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @Column(nullable = false)
    private String content;
}

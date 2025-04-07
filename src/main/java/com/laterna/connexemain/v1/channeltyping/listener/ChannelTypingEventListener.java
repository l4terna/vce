package com.laterna.connexemain.v1.channeltyping.listener;

import com.laterna.connexemain.v1.channeltyping.event.ChannelTypingStatusChangeEvent;
import com.laterna.connexemain.v1.channeltyping.messaging.ChannelTypingProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelTypingEventListener {
    private final ChannelTypingProducer channelTypingProducer;

    @EventListener
    public void handleTypingStatusChange(ChannelTypingStatusChangeEvent event) {
        channelTypingProducer.sendTypingStatus(event);
    }
}

package com.laterna.connexemain.v1.messageread.enumeration;

import lombok.Getter;

@Getter
public enum MessageStatus {
    SENT(0),
    READ(1),
    NEW(2);

    private final int value;

    MessageStatus(int code) {
        this.value = code;
    }

}

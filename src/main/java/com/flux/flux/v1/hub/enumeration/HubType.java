package com.flux.flux.v1.hub.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum HubType {
    PRIVATE(0),
    PUBLIC(1);

    private final int value;

    HubType(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    @JsonCreator
    public static HubType fromValue(int value) {
        for (HubType type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ChannelType value: " + value);
    }
}

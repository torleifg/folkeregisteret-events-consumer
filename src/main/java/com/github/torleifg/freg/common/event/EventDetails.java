package com.github.torleifg.freg.common.event;

import lombok.Value;

@Value
public class EventDetails {
    EventType eventType;
    String personIdentifier;
    String eventDocumentId;
}
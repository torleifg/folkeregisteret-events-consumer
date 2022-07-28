package com.github.torleifg.freg.event.adapter.out.web.freg.eventdocument;

import com.github.torleifg.freg.common.event.EventType;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EventDocumentMappersWrapper {
    private final Map<EventType, EventDocumentMapper> mappers;

    private EventDocumentMappersWrapper(Map<EventType, EventDocumentMapper> mapper) {
        this.mappers = mapper;
    }

    public static EventDocumentMappersWrapper from(List<EventDocumentMapper> mappers) {
        return new EventDocumentMappersWrapper(mappers.stream()
                .collect(Collectors.toUnmodifiableMap(EventDocumentMapper::supports, Function.identity())));
    }

    public EventDocumentMapper getMapper(EventType eventType) {
        return mappers.get(eventType);
    }
}

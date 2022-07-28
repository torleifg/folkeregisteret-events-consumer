package com.github.torleifg.freg.person.application.handler;

import com.github.torleifg.freg.common.event.EventType;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EventDocumentHandlersWrapper {
    private final Map<EventType, EventDocumentHandler> handlers;

    private EventDocumentHandlersWrapper(Map<EventType, EventDocumentHandler> handlers) {
        this.handlers = handlers;
    }

    public static EventDocumentHandlersWrapper from(List<EventDocumentHandler> handlers) {
        return new EventDocumentHandlersWrapper(handlers.stream()
                .collect(Collectors.toUnmodifiableMap(EventDocumentHandler::supports, Function.identity())));
    }

    public EventDocumentHandler getHandler(EventType eventType) {
        return handlers.get(eventType);
    }
}

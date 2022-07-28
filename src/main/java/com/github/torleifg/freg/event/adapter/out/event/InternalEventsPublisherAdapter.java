package com.github.torleifg.freg.event.adapter.out.event;

import com.github.torleifg.freg.common.event.Event;
import com.github.torleifg.freg.event.application.port.out.InternalEventsPublisherService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

@Component
public class InternalEventsPublisherAdapter implements InternalEventsPublisherService {
    private final Sinks.Many<Event> internalEventsBus;

    public InternalEventsPublisherAdapter(Sinks.Many<Event> internalEventsBus) {
        this.internalEventsBus = internalEventsBus;
    }

    @Override
    public Sinks.EmitResult produceEvent(Event event) {
        return internalEventsBus.tryEmitNext(event);
    }
}
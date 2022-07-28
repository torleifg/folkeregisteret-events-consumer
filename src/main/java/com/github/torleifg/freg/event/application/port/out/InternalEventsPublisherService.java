package com.github.torleifg.freg.event.application.port.out;

import com.github.torleifg.freg.common.event.Event;
import reactor.core.publisher.Sinks;

public interface InternalEventsPublisherService {
    Sinks.EmitResult produceEvent(Event event);
}
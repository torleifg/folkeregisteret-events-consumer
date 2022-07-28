package com.github.torleifg.freg.event.application.port.out;

import com.github.torleifg.freg.common.eventdocument.EventDocument;
import com.github.torleifg.freg.common.event.Event;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FregEventsService {
    Mono<Long> getExternalSequence();

    Flux<Event> getExternalEvents(Long sequence);

    Mono<EventDocument> getEventDocument(String id);
}
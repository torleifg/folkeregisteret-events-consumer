package com.github.torleifg.freg.person.application.port.out;

import com.github.torleifg.freg.common.eventdocument.EventDocument;
import reactor.core.publisher.Mono;

public interface EventDocumentService {
    Mono<EventDocument> getEventDocument(String id);
}
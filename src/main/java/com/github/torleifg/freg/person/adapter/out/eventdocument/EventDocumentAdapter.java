package com.github.torleifg.freg.person.adapter.out.eventdocument;

import com.github.torleifg.freg.common.eventdocument.EventDocument;
import com.github.torleifg.freg.event.application.port.out.FregEventsService;
import com.github.torleifg.freg.person.application.port.out.EventDocumentService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class EventDocumentAdapter implements EventDocumentService {
    private final FregEventsService fregEventsService;

    public EventDocumentAdapter(FregEventsService fregEventsService) {
        this.fregEventsService = fregEventsService;
    }

    @Override
    public Mono<EventDocument> getEventDocument(String id) {
        return fregEventsService.getEventDocument(id);
    }
}
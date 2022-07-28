package com.github.torleifg.freg.event.adapter.out.web;

import com.github.torleifg.freg.common.eventdocument.EventDocument;
import com.github.torleifg.freg.event.adapter.out.web.freg.event.EventMapper;
import com.github.torleifg.freg.event.adapter.out.web.freg.event.EventTypesMappingWrapper;
import com.github.torleifg.freg.event.adapter.out.web.freg.FregConnector;
import com.github.torleifg.freg.event.adapter.out.web.freg.eventdocument.EventDocumentMappersWrapper;
import com.github.torleifg.freg.event.adapter.out.web.maskinporten.MaskinportenConnector;
import com.github.torleifg.freg.event.application.port.out.FregEventsService;
import com.github.torleifg.freg.common.event.Event;
import no.skatteetaten.folkeregisteret.model.FolkeregisteretTilgjengeliggjoeringHendelseV1DokumentForHendelse;
import no.skatteetaten.folkeregisteret.model.FolkeregisteretTilgjengeliggjoeringHendelseV1Hendelse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Optional;

@Component
public class RestAdapter implements FregEventsService {
    private final MaskinportenConnector maskinportenConnector;
    private final FregConnector fregConnector;
    private final EventMapper eventMapper;
    private final EventDocumentMappersWrapper eventDocumentMappers;

    public RestAdapter(MaskinportenConnector maskinportenConnector, FregConnector fregConnector, EventMapper eventMapper, EventDocumentMappersWrapper eventDocumentMappers) {
        this.maskinportenConnector = maskinportenConnector;
        this.fregConnector = fregConnector;
        this.eventMapper = eventMapper;
        this.eventDocumentMappers = eventDocumentMappers;
    }

    @Override
    public Mono<Long> getExternalSequence() {
        return maskinportenConnector.getAccessToken()
                .flatMap(fregConnector::getExternalSequence);
    }

    @Override
    public Flux<Event> getExternalEvents(Long sequence) {
        return maskinportenConnector.getAccessToken()
                .flatMapMany(accessToken -> fregConnector.getEvents(accessToken, sequence))
                .map(eventMapper::toDomain);
    }

    @Override
    public Mono<EventDocument> getEventDocument(String id) {
        return maskinportenConnector.getAccessToken()
                .flatMap(accessToken -> fregConnector.getEventDocument(accessToken, id))
                .map(FolkeregisteretTilgjengeliggjoeringHendelseV1DokumentForHendelse::getHendelse)
                .map(this::toDomain);
    }

    private EventDocument toDomain(FolkeregisteretTilgjengeliggjoeringHendelseV1Hendelse event) {
        final var eventDocumentMapper = eventDocumentMappers.getMapper(EventTypesMappingWrapper.get(event.getHendelsestype()));

        return Optional.ofNullable(event.getEgenskapshendelse())
                .orElse(Collections.emptyList())
                .stream()
                .findFirst()
                .map(eventDocumentMapper::toDomain)
                .orElseThrow();
    }
}
package com.github.torleifg.freg.event.adapter.out.web.freg.event;

import com.github.torleifg.freg.common.event.Event;
import com.github.torleifg.freg.common.event.EventDetails;
import com.github.torleifg.freg.common.event.EventType;
import no.skatteetaten.folkeregisteret.model.SkeFolkeregisterRettighetspakkeHendelserHendelseMedDokumentIdentifikatorer;
import no.skatteetaten.folkeregisteret.model.SkeFolkeregisterRettighetspakkeHendelserLagretHendelseMedDokumentIdentifikatorer;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EventMapper {

    public Event toDomain(SkeFolkeregisterRettighetspakkeHendelserLagretHendelseMedDokumentIdentifikatorer fregEvent) {
        final var fregEventDetails = Optional.ofNullable(fregEvent.getHendelse())
                .orElseThrow();

        return new Event(fregEvent.getSekvensnummer(), getEventDetails(fregEventDetails));
    }

    private EventDetails getEventDetails(SkeFolkeregisterRettighetspakkeHendelserHendelseMedDokumentIdentifikatorer fregEventDetails) {
        return new EventDetails(getEventType(fregEventDetails), getPersonIdentifier(fregEventDetails), getEventDocumentId(fregEventDetails));
    }

    private EventType getEventType(SkeFolkeregisterRettighetspakkeHendelserHendelseMedDokumentIdentifikatorer fregEventDetails) {
        return EventTypesMappingWrapper.get(fregEventDetails.getHendelsetype());
    }

    private String getPersonIdentifier(SkeFolkeregisterRettighetspakkeHendelserHendelseMedDokumentIdentifikatorer fregEventDetails) {
        return fregEventDetails.getFolkeregisteridentifikator();
    }

    private String getEventDocumentId(SkeFolkeregisterRettighetspakkeHendelserHendelseMedDokumentIdentifikatorer fregEventDetails) {
        return fregEventDetails.getHendelsesdokument();
    }
}
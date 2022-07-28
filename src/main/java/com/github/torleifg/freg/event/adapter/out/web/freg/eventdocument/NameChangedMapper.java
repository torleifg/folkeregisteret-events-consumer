package com.github.torleifg.freg.event.adapter.out.web.freg.eventdocument;

import com.github.torleifg.freg.common.event.EventType;
import com.github.torleifg.freg.common.eventdocument.EventDocument;
import com.github.torleifg.freg.common.eventdocument.NameChangedEventDocument;
import no.skatteetaten.folkeregisteret.model.FolkeregisteretTilgjengeliggjoeringHendelseV1Registerdata;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NameChangedMapper implements EventDocumentMapper {

    @Override
    public EventDocument toDomain(FolkeregisteretTilgjengeliggjoeringHendelseV1Registerdata registerData) {
        final var name = Optional.ofNullable(registerData.getNavn())
                .orElseThrow();

        return new NameChangedEventDocument(normalize(name.getFornavn()), normalize(name.getEtternavn()));
    }

    @Override
    public EventType supports() {
        return EventType.CHANGE_IN_NAME;
    }

    private String normalize(String text) {
        return StringUtils.capitalize(text.toLowerCase());
    }
}
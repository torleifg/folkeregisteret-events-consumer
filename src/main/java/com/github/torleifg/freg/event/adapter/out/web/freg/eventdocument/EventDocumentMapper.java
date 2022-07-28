package com.github.torleifg.freg.event.adapter.out.web.freg.eventdocument;

import com.github.torleifg.freg.common.event.EventType;
import com.github.torleifg.freg.common.eventdocument.EventDocument;
import no.skatteetaten.folkeregisteret.model.FolkeregisteretTilgjengeliggjoeringHendelseV1Registerdata;

public interface EventDocumentMapper {

    EventDocument toDomain(FolkeregisteretTilgjengeliggjoeringHendelseV1Registerdata registerData);

    EventType supports();
}
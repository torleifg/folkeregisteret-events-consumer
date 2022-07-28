package com.github.torleifg.freg.person.application.handler;

import com.github.torleifg.freg.common.event.EventType;
import com.github.torleifg.freg.common.eventdocument.EventDocument;
import com.github.torleifg.freg.person.domain.Person;

public interface EventDocumentHandler {

    Person handle(String id, EventDocument eventDocument);

    EventType supports();
}
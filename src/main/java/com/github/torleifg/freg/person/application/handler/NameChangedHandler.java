package com.github.torleifg.freg.person.application.handler;

import com.github.torleifg.freg.common.event.EventType;
import com.github.torleifg.freg.common.eventdocument.EventDocument;
import com.github.torleifg.freg.common.eventdocument.NameChangedEventDocument;
import com.github.torleifg.freg.person.domain.Person;
import org.springframework.stereotype.Component;

@Component
public class NameChangedHandler implements EventDocumentHandler {

    @Override
    public Person handle(String id, EventDocument eventDocument) {
        if (eventDocument instanceof NameChangedEventDocument) {
            final var nameChangedEventDocument = (NameChangedEventDocument) eventDocument;

            return new Person(id, nameChangedEventDocument.getGivenName() + " " + nameChangedEventDocument.getFamilyName());
        } else {
            throw new ClassCastException("");
        }
    }

    @Override
    public EventType supports() {
        return EventType.CHANGE_IN_NAME;
    }
}
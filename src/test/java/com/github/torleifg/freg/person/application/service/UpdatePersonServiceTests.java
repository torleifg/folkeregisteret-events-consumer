package com.github.torleifg.freg.person.application.service;

import com.github.torleifg.freg.common.event.Event;
import com.github.torleifg.freg.common.event.EventDetails;
import com.github.torleifg.freg.common.event.EventType;
import com.github.torleifg.freg.common.eventdocument.NameChangedEventDocument;
import com.github.torleifg.freg.person.adapter.out.db.PersonEntity;
import com.github.torleifg.freg.person.application.handler.EventDocumentHandlersWrapper;
import com.github.torleifg.freg.person.application.port.out.EventDocumentService;
import com.github.torleifg.freg.person.application.port.out.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@SpringBootTest
class UpdatePersonServiceTests {
    @Autowired
    R2dbcEntityTemplate template;

    @Autowired
    PersonRepository personRepository;

    @MockBean
    EventDocumentService eventDocumentService;

    @Autowired
    EventDocumentHandlersWrapper eventDocumentHandlers;

    UpdatePersonService updatePersonService;

    @BeforeEach
    void setUp() {
        updatePersonService = new UpdatePersonService(personRepository, eventDocumentService, eventDocumentHandlers);

        template.delete(PersonEntity.class)
                .all()
                .block();
    }

    @Test
    void updatePersonIfExistsTest() {
        var person = new PersonEntity("12345", "Firstname Lastname");
        var event = new Event(1L, new EventDetails(EventType.CHANGE_IN_NAME, "12345", "id"));
        var eventDocument = new NameChangedEventDocument("Givenname", "Familyname");

        template.insert(PersonEntity.class)
                .using(person)
                .block();

        when(eventDocumentService.getEventDocument("id")).thenReturn(Mono.just(eventDocument));

        updatePersonService.updatePerson(event)
                .as(StepVerifier::create)
                .expectNextMatches(p -> p.getId().equals("12345") && p.getName().equals("Givenname Familyname"))
                .verifyComplete();
    }

    @Test
    void updatePersonIfNotExistsTest() {
        var event = new Event(1L, new EventDetails(EventType.CHANGE_IN_NAME, "12345", "id"));

        updatePersonService.updatePerson(event)
                .as(StepVerifier::create)
                .verifyComplete();
    }
}
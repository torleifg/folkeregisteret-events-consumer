package com.github.torleifg.freg.person.application.service;

import com.github.torleifg.freg.common.event.Event;
import com.github.torleifg.freg.person.application.handler.EventDocumentHandlersWrapper;
import com.github.torleifg.freg.person.application.port.in.UpdatePersonUseCase;
import com.github.torleifg.freg.person.application.port.out.EventDocumentService;
import com.github.torleifg.freg.person.application.port.out.PersonRepository;
import com.github.torleifg.freg.person.domain.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class UpdatePersonService implements UpdatePersonUseCase {
    private final PersonRepository personRepository;
    private final EventDocumentService eventDocumentService;
    private final EventDocumentHandlersWrapper eventDocumentHandlers;

    public UpdatePersonService(PersonRepository personRepository, EventDocumentService eventDocumentService,
                               EventDocumentHandlersWrapper eventDocumentHandlers) {
        this.personRepository = personRepository;
        this.eventDocumentService = eventDocumentService;
        this.eventDocumentHandlers = eventDocumentHandlers;
    }

    @Override
    public Mono<Person> updatePerson(Event event) {
        return personRepository.findById(event.getEventDetails().getPersonIdentifier())
                .flatMap(person -> handleEvent(person, event))
                .flatMap(personRepository::save);
    }

    private Mono<Person> handleEvent(Person person, Event event) {
        return eventDocumentService.getEventDocument(event.getEventDetails().getEventDocumentId())
                .map(eventDocument -> eventDocumentHandlers.getHandler(event.getEventDetails().getEventType())
                        .handle(person.getId(), eventDocument));
    }
}
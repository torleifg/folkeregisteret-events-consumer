package com.github.torleifg.freg.person.application.port.in;

import com.github.torleifg.freg.common.event.Event;
import com.github.torleifg.freg.person.domain.Person;
import reactor.core.publisher.Mono;

public interface UpdatePersonUseCase {
    Mono<Person> updatePerson(Event event);
}

package com.github.torleifg.freg.person.application.port.in;

import com.github.torleifg.freg.person.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetPersonUseCase {
    Flux<Person> findAll();

    Mono<Person> findById(String id);
}
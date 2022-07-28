package com.github.torleifg.freg.person.application.port.out;

import com.github.torleifg.freg.person.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonRepository {

    Mono<Person> save(Person person);

    Flux<Person> findAll();

    Mono<Person> findById(String id);
}
package com.github.torleifg.freg.person.application.service;

import com.github.torleifg.freg.person.application.port.in.GetPersonUseCase;
import com.github.torleifg.freg.person.application.port.out.PersonRepository;
import com.github.torleifg.freg.person.domain.Person;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GetPersonService implements GetPersonUseCase {
    private final PersonRepository personRepository;

    public GetPersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Flux<Person> findAll() {
        return personRepository.findAll();
    }

    @Override
    public Mono<Person> findById(String id) {
        return personRepository.findById(id);
    }
}
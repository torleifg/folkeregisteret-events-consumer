package com.github.torleifg.freg.person.adapter.out.db;

import com.github.torleifg.freg.person.application.port.out.PersonRepository;
import com.github.torleifg.freg.person.domain.Person;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class PersonRepositoryAdapter implements PersonRepository {
    private final PersonCrudRepository personCrudRepository;
    private final PersonMapper personMapper;

    public PersonRepositoryAdapter(PersonCrudRepository personCrudRepository, PersonMapper personMapper) {
        this.personCrudRepository = personCrudRepository;
        this.personMapper = personMapper;
    }

    @Override
    public Mono<Person> save(Person person) {
        return personCrudRepository.save(personMapper.toEntity(person))
                .map(personMapper::toDomain);
    }

    @Override
    public Flux<Person> findAll() {
        return personCrudRepository.findAll()
                .map(personMapper::toDomain);
    }

    @Override
    public Mono<Person> findById(String id) {
        return personCrudRepository.findById(id)
                .map(personMapper::toDomain);
    }
}
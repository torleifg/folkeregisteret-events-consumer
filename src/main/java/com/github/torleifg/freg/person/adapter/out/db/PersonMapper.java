package com.github.torleifg.freg.person.adapter.out.db;

import com.github.torleifg.freg.person.domain.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {

    public PersonEntity toEntity(Person person) {
        return new PersonEntity(person.getId(), person.getName());
    }

    public Person toDomain(PersonEntity personEntity) {
        return new Person(personEntity.getId(), personEntity.getName());
    }
}
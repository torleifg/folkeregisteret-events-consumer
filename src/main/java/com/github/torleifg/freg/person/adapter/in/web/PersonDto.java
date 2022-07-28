package com.github.torleifg.freg.person.adapter.in.web;

import com.github.torleifg.freg.person.domain.Person;
import lombok.Value;

@Value
public class PersonDto {
    String id;
    String name;

    public static PersonDto from(Person person) {
        return new PersonDto(person.getId(), person.getName());
    }
}

package com.github.torleifg.freg.person.adapter.out.db;

import com.github.torleifg.freg.person.domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.test.StepVerifier;

@DataR2dbcTest
@Import(PersonMapper.class)
class PersonRepositoryAdapterTests {

    @Autowired
    R2dbcEntityTemplate template;

    @Autowired
    PersonCrudRepository personCrudRepository;

    @Autowired
    PersonMapper mapper;

    PersonRepositoryAdapter personRepositoryAdapter;

    @BeforeEach
    void setUp() {
        personRepositoryAdapter = new PersonRepositoryAdapter(personCrudRepository, mapper);

        template.delete(PersonEntity.class)
                .all()
                .block();
    }

    @Test
    void saveTest() {
        template.insert(PersonEntity.class)
                .using(new PersonEntity("12345", "Firstname Lastname"))
                .block();

        personRepositoryAdapter.save(new Person("12345", "Givenname Familyname"))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void findAllTest() {
        template.insert(PersonEntity.class)
                .using(new PersonEntity("12345", "Firstname Lastname"))
                .block();

        personRepositoryAdapter.findAll()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void findOneTest() {
        template.insert(PersonEntity.class)
                .using(new PersonEntity("12345", "Firstname Lastname"))
                .block();

        personRepositoryAdapter.findById("12345")
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }
}
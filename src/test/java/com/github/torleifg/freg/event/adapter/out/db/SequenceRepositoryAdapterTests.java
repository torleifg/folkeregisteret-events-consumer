package com.github.torleifg.freg.event.adapter.out.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.test.StepVerifier;

@DataR2dbcTest
class SequenceRepositoryAdapterTests {

    @Autowired
    R2dbcEntityTemplate template;

    SequenceRepositoryAdapter sequenceRepositoryAdapter;

    @BeforeEach
    void setUp() {
        sequenceRepositoryAdapter = new SequenceRepositoryAdapter(template);

        template.delete(SequenceEntity.class)
                .all()
                .block();
    }

    @Test
    void getInternalSequenceTest() {
        template.insert(SequenceEntity.class)
                .using(new SequenceEntity(1L))
                .block();

        sequenceRepositoryAdapter.getInternalSequence()
                .as(StepVerifier::create)
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void updateInternalSequenceTest() {
        template.insert(SequenceEntity.class)
                .using(new SequenceEntity(1L))
                .block();

        sequenceRepositoryAdapter.updateInternalSequence(2L)
                .flatMap(it -> template.select(SequenceEntity.class)
                        .one()
                        .map(SequenceEntity::getId))
                .as(StepVerifier::create)
                .expectNext(2L)
                .verifyComplete();
    }
}
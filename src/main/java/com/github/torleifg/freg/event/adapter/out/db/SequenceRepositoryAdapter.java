package com.github.torleifg.freg.event.adapter.out.db;

import com.github.torleifg.freg.event.application.port.out.SequenceRepository;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class SequenceRepositoryAdapter implements SequenceRepository {
    private static final String COLUMN = "id";

    private final R2dbcEntityTemplate template;

    public SequenceRepositoryAdapter(R2dbcEntityTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<Long> updateInternalSequence(Long internalSequence) {
        return template.update(SequenceEntity.class)
                .apply(Update.update(COLUMN, internalSequence))
                .thenReturn(internalSequence);
    }

    @Override
    public Mono<Long> getInternalSequence() {
        return template.select(SequenceEntity.class)
                .one()
                .map(SequenceEntity::getId);
    }
}
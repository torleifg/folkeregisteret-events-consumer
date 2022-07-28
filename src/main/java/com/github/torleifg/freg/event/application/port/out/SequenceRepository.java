package com.github.torleifg.freg.event.application.port.out;

import reactor.core.publisher.Mono;

public interface SequenceRepository {
    Mono<Long> updateInternalSequence(Long internalSequence);

    Mono<Long> getInternalSequence();
}

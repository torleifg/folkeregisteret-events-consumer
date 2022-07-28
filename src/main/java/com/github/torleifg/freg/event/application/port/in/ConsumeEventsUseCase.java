package com.github.torleifg.freg.event.application.port.in;

import reactor.core.publisher.Mono;

public interface ConsumeEventsUseCase {
    Mono<Long> consumeEvents();
}
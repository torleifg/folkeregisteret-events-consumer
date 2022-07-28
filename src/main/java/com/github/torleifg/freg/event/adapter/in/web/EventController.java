package com.github.torleifg.freg.event.adapter.in.web;

import com.github.torleifg.freg.event.application.port.in.ConsumeEventsUseCase;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("events")
public class EventController {
    private final ConsumeEventsUseCase useCase;

    public EventController(ConsumeEventsUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public Mono<Long> consumeEvents() {
        return useCase.consumeEvents();
    }
}
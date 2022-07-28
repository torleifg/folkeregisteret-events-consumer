package com.github.torleifg.freg.person.adapter.in.event;

import com.github.torleifg.freg.common.event.Event;
import com.github.torleifg.freg.person.application.port.in.UpdatePersonUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class InternalEventsSubscriberAdapter {
    private final UpdatePersonUseCase updatePersonUseCase;
    private final Sinks.Many<Event> internalEventsBus;

    public InternalEventsSubscriberAdapter(UpdatePersonUseCase updatePersonUseCase, Sinks.Many<Event> internalEventsBus) {
        this.updatePersonUseCase = updatePersonUseCase;
        this.internalEventsBus = internalEventsBus;
    }

    @PostConstruct
    public void init() {
        listen();
    }

    public void listen() {
        internalEventsBus.asFlux()
                .flatMap(updatePersonUseCase::updatePerson)
                .doOnNext(person -> log.info(String.format("Saved %s", person)))
                .subscribe();
    }
}
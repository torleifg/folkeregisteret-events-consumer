package com.github.torleifg.freg.event.application.service;

import com.github.torleifg.freg.common.event.EventType;
import com.github.torleifg.freg.event.application.port.in.ConsumeEventsUseCase;
import com.github.torleifg.freg.event.application.port.out.FregEventsService;
import com.github.torleifg.freg.event.application.port.out.InternalEventsPublisherService;
import com.github.torleifg.freg.event.application.port.out.SequenceRepository;
import com.github.torleifg.freg.common.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

import java.time.Duration;
import java.util.Set;

@Slf4j
@Service
public class ConsumeEventsService implements ConsumeEventsUseCase {
    private static final int PAGE_SIZE = 1000;

    private final FregEventsService fregEventsService;
    private final SequenceRepository sequenceRepository;
    private final InternalEventsPublisherService internalEventsPublisherService;
    private final Set<EventType> eventTypes;

    public ConsumeEventsService(FregEventsService fregEventsService, SequenceRepository sequenceRepository,
                                InternalEventsPublisherService internalEventsPublisherService,
                                @Value("${events.include}") Set<EventType> eventTypes) {
        this.fregEventsService = fregEventsService;
        this.sequenceRepository = sequenceRepository;
        this.internalEventsPublisherService = internalEventsPublisherService;
        this.eventTypes = eventTypes;
    }

    @Override
    public Mono<Long> consumeEvents() {
        return sequenceRepository.getInternalSequence()
                .flatMap(internalSequence -> fregEventsService.getExternalSequence()
                        .flatMapMany(externalSequence -> Flux.range(0, count(externalSequence, internalSequence)))
                        .delayElements(Duration.ofMillis(1000))
                        .flatMapSequential(count -> fregEventsService.getExternalEvents(internalSequence + (count * PAGE_SIZE)))
                        .handle(this::handleEvent)
                        .last(internalSequence)
                        .flatMap(sequenceRepository::updateInternalSequence));
    }

    private void handleEvent(Event event, SynchronousSink<Long> synchronousSink) {
        if (eventTypes.contains(event.getEventDetails().getEventType())) {
            final var result = internalEventsPublisherService.produceEvent(event);

            log.info(String.format("%s %s", result.isSuccess() ? "Sent" : "Failed to send", event));
        }

        synchronousSink.next(event.getSequence());
    }

    private int count(Long externalSequence, Long internalSequence) {
        log.info("start: " + internalSequence + " end: " + externalSequence);

        final var diff = externalSequence - internalSequence;

        return (int) (diff / PAGE_SIZE + (diff % PAGE_SIZE == 0 ? 0 : 1));
    }
}
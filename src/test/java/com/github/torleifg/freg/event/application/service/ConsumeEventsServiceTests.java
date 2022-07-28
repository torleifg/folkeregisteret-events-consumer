package com.github.torleifg.freg.event.application.service;

import com.github.torleifg.freg.common.event.EventDetails;
import com.github.torleifg.freg.common.event.EventType;
import com.github.torleifg.freg.event.adapter.out.db.SequenceEntity;
import com.github.torleifg.freg.event.application.port.out.FregEventsService;
import com.github.torleifg.freg.event.application.port.out.InternalEventsPublisherService;
import com.github.torleifg.freg.event.application.port.out.SequenceRepository;
import com.github.torleifg.freg.common.event.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.mockito.Mockito.*;

@SpringBootTest
class ConsumeEventsServiceTests {
    @Autowired
    R2dbcEntityTemplate template;

    @MockBean
    FregEventsService fregEventsService;

    @Autowired
    SequenceRepository sequenceRepository;

    @MockBean
    InternalEventsPublisherService internalEventsPublisherService;

    @Value("${events.include}")
    Set<EventType> eventTypes;

    ConsumeEventsService consumeEventsService;

    @BeforeEach
    void setUp() {
        consumeEventsService = new ConsumeEventsService(fregEventsService, sequenceRepository, internalEventsPublisherService, eventTypes);

        template.delete(SequenceEntity.class)
                .all()
                .block();
    }

    @Test
    void consumeEventsGivenInternalSequenceLessThanExternalSequenceTest() {
        var event = new Event(2L, new EventDetails(EventType.CHANGE_IN_NAME, "12345", "id"));

        template.insert(SequenceEntity.class)
                .using(new SequenceEntity(1L))
                .block();

        when(fregEventsService.getExternalSequence()).thenReturn(Mono.just(2L));
        when(fregEventsService.getExternalEvents(1L)).thenReturn(Flux.just(event));

        when(internalEventsPublisherService.produceEvent(event)).thenReturn(Sinks.EmitResult.OK);

        consumeEventsService.consumeEvents()
                .as(StepVerifier::create)
                .expectNext(2L)
                .verifyComplete();
    }

    @Test
    void consumeEventsGivenInternalSequenceEqualToExternalSequenceTest() {
        template.insert(SequenceEntity.class)
                .using(new SequenceEntity(1L))
                .block();

        when(fregEventsService.getExternalSequence()).thenReturn(Mono.just(1L));

        consumeEventsService.consumeEvents()
                .as(StepVerifier::create)
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void consumeEventsGivenNotIncludedEventTypeTest() {
        var event = new Event(2L, new EventDetails(EventType.CHANGE_IN_BIRTH, "12345", "id"));

        template.insert(SequenceEntity.class)
                .using(new SequenceEntity(1L))
                .block();

        when(fregEventsService.getExternalSequence()).thenReturn(Mono.just(2L));
        when(fregEventsService.getExternalEvents(1L)).thenReturn(Flux.just(event));

        consumeEventsService.consumeEvents()
                .as(StepVerifier::create)
                .expectNext(2L)
                .verifyComplete();
    }
}
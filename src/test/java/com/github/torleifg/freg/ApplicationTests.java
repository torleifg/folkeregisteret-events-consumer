package com.github.torleifg.freg;

import com.github.torleifg.freg.common.event.Event;
import com.github.torleifg.freg.common.event.EventDetails;
import com.github.torleifg.freg.common.event.EventType;
import com.github.torleifg.freg.common.eventdocument.NameChangedEventDocument;
import com.github.torleifg.freg.event.adapter.out.db.SequenceEntity;
import com.github.torleifg.freg.event.application.port.out.FregEventsService;
import com.github.torleifg.freg.person.adapter.out.db.PersonEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTests {

    @LocalServerPort
    int port;

    @Autowired
    R2dbcEntityTemplate template;

    @MockBean
    FregEventsService fregEventsService;

    @BeforeEach
    void setUp() {
        template.delete(SequenceEntity.class)
                .all()
                .block();

        template.delete(PersonEntity.class)
                .all()
                .block();
    }

    @Test
    void givenNewEventAndPersonExistsThenSequenceIsIncrementedAndNameOfPersonIsChangedTest() {
        template.insert(SequenceEntity.class)
                .using(new SequenceEntity(0L))
                .block();

        template.insert(PersonEntity.class)
                .using(new PersonEntity("12345", "Firstname Lastname"))
                .block();

        Mockito.when(fregEventsService.getExternalSequence()).thenReturn(Mono.just(2L));

        Mockito.when(fregEventsService.getExternalEvents(anyLong())).thenReturn(Flux.just(
                new Event(1L, new EventDetails(EventType.CHANGE_IN_NAME, "12345", "id-1")),
                new Event(2L, new EventDetails(EventType.CHANGE_IN_NAME, "12345", "id-2")))
        );

        Mockito.when(fregEventsService.getEventDocument(anyString()))
                .thenReturn(Mono.just(new NameChangedEventDocument("Given", "Family")))
                .thenReturn(Mono.just(new NameChangedEventDocument("Givenname", "Familyname")));

        var sequence = given()
                .port(port)
                .when()
                .post("/events")
                .then()
                .log().ifError()
                .statusCode(200)
                .extract()
                .as(Long.class);

        assertEquals(2L, sequence);

        var person = template.select(PersonEntity.class)
                .one()
                .block();

        assertNotNull(person);
        assertEquals("Givenname Familyname", person.getName());
    }

    @Test
    void givenNoNewEventThenSequenceIsNotIncrementedTest() {
        template.insert(SequenceEntity.class)
                .using(new SequenceEntity(0L))
                .block();

        Mockito.when(fregEventsService.getExternalSequence()).thenReturn(Mono.just(0L));

        var sequence = given()
                .port(port)
                .when()
                .post("/events")
                .then()
                .log().ifError()
                .statusCode(200)
                .extract()
                .as(Long.class);

        assertEquals(0L, sequence);
    }

    @Test
    void givenNewEventAndPersonNotExistsThenSequenceIsIncrementedTest() {
        template.insert(SequenceEntity.class)
                .using(new SequenceEntity(0L))
                .block();

        template.insert(PersonEntity.class)
                .using(new PersonEntity("12345", "Firstname Lastname"))
                .block();


        Mockito.when(fregEventsService.getExternalSequence()).thenReturn(Mono.just(1L));

        Mockito.when(fregEventsService.getExternalEvents(anyLong())).thenReturn(Flux.just(
                new Event(1L, new EventDetails(EventType.CHANGE_IN_NAME, "6789", "id")))
        );

        var sequence = given()
                .port(port)
                .when()
                .post("/events")
                .then()
                .log().ifError()
                .statusCode(200)
                .extract()
                .as(Long.class);

        assertEquals(1L, sequence);

        var person = template.select(PersonEntity.class)
                .one()
                .block();

        assertNotNull(person);
        assertEquals("Firstname Lastname", person.getName());
    }

    @Test
    void givenNewEventAndPersonExistsAndNotIncludedEventTypeThenSequenceIsIncrementedTest() {
        template.insert(SequenceEntity.class)
                .using(new SequenceEntity(0L))
                .block();

        template.insert(PersonEntity.class)
                .using(new PersonEntity("12345", "Firstname Lastname"))
                .block();

        Mockito.when(fregEventsService.getExternalSequence()).thenReturn(Mono.just(1L));

        Mockito.when(fregEventsService.getExternalEvents(anyLong())).thenReturn(Flux.just(
                new Event(1L, new EventDetails(EventType.CHANGE_IN_BIRTH, "12345", "id")))
        );

        var sequence = given()
                .port(port)
                .when()
                .post("/events")
                .then()
                .log().ifError()
                .statusCode(200)
                .extract()
                .as(Long.class);

        assertEquals(1L, sequence);

        var person = template.select(PersonEntity.class)
                .one()
                .block();

        assertNotNull(person);
        assertEquals("Firstname Lastname", person.getName());
    }
}
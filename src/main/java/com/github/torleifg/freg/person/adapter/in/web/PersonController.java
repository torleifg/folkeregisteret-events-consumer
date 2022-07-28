package com.github.torleifg.freg.person.adapter.in.web;

import com.github.torleifg.freg.person.application.port.in.GetPersonUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("person")
public class PersonController {
    private final GetPersonUseCase useCase;

    public PersonController(GetPersonUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping
    public Flux<PersonDto> findAll() {
        return useCase.findAll()
                .map(PersonDto::from);
    }

    @GetMapping("/{id}")
    public Mono<PersonDto> findById(@PathVariable String id) {
        return useCase.findById(id)
                .switchIfEmpty(Mono.error(NotFoundException::new))
                .map(PersonDto::from);
    }

    @RestControllerAdvice
    static class ControllerExceptionHandler {

        @ExceptionHandler(NotFoundException.class)
        public ResponseEntity<?> handleWebClientResponseException(NotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
package com.github.torleifg.freg.event.adapter.out.web;

import com.github.torleifg.freg.event.adapter.out.web.freg.eventdocument.EventDocumentMapper;
import com.github.torleifg.freg.event.adapter.out.web.freg.eventdocument.EventDocumentMappersWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Configuration
public class RestAdapterConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public EventDocumentMappersWrapper eventDocumentMappers(List<EventDocumentMapper> mappers) {
        return EventDocumentMappersWrapper.from(mappers);
    }
}
package com.github.torleifg.freg.person.application.config;

import com.github.torleifg.freg.person.application.handler.EventDocumentHandler;
import com.github.torleifg.freg.person.application.handler.EventDocumentHandlersWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PersonConfig {

    @Bean
    public EventDocumentHandlersWrapper eventDocumentHandlers(List<EventDocumentHandler> handlers) {
        return EventDocumentHandlersWrapper.from(handlers);
    }
}
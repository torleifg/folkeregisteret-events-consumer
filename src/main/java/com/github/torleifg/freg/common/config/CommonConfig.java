package com.github.torleifg.freg.common.config;

import com.github.torleifg.freg.common.event.Event;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class CommonConfig {

    @Bean
    public Sinks.Many<Event> internalEventBus() {
        return Sinks.many().unicast().onBackpressureBuffer();
    }
}
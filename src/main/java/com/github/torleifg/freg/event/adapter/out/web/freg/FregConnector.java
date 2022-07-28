package com.github.torleifg.freg.event.adapter.out.web.freg;

import no.skatteetaten.folkeregisteret.model.FolkeregisteretTilgjengeliggjoeringHendelseV1DokumentForHendelse;
import no.skatteetaten.folkeregisteret.model.SkeFolkeregisterRettighetspakkeHendelserLagretHendelseMedDokumentIdentifikatorer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class FregConnector {
    private static final String SEQUENCE = "seq";

    private final WebClient webClient;
    private final FregProperties fregProperties;

    public FregConnector(WebClient webClient, FregProperties fregProperties) {
        this.webClient = webClient;
        this.fregProperties = fregProperties;
    }

    public Mono<Long> getExternalSequence(String accessToken) {
        return webClient.get()
                .uri(fregProperties.getBaseUrl(), uriBuilder -> uriBuilder
                        .path(fregProperties.getSequence())
                        .build())
                .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(Long.class);
    }

    public Flux<SkeFolkeregisterRettighetspakkeHendelserLagretHendelseMedDokumentIdentifikatorer> getEvents(String accessToken, Long sequence) {
        return webClient.get()
                .uri(fregProperties.getBaseUrl(), uriBuilder -> uriBuilder
                        .path(fregProperties.getFeed())
                        .queryParam(SEQUENCE, sequence)
                        .build())
                .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
                .retrieve()
                .bodyToFlux(SkeFolkeregisterRettighetspakkeHendelserLagretHendelseMedDokumentIdentifikatorer.class);
    }

    public Mono<FolkeregisteretTilgjengeliggjoeringHendelseV1DokumentForHendelse> getEventDocument(String accessToken, String id) {
        return webClient.get()
                .uri(fregProperties.getBaseUrl(), uriBuilder -> uriBuilder
                        .path(fregProperties.getEventDocument())
                        .build(id))
                .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(FolkeregisteretTilgjengeliggjoeringHendelseV1DokumentForHendelse.class);
    }
}
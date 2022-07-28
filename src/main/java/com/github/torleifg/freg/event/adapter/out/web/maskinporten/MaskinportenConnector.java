package com.github.torleifg.freg.event.adapter.out.web.maskinporten;

import com.nimbusds.oauth2.sdk.GrantType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.*;

@Component
public class MaskinportenConnector {
    private final WebClient webClient;
    private final JwtBearerGrantGenerator jwtBearerGrantGenerator;
    private final URI tokenEndpointURI;

    public MaskinportenConnector(WebClient webClient, JwtBearerGrantGenerator jwtBearerGrantGenerator, URI tokenEndpointURI) {
        this.webClient = webClient;
        this.jwtBearerGrantGenerator = jwtBearerGrantGenerator;
        this.tokenEndpointURI = tokenEndpointURI;
    }

    public Mono<JwtBearerGrantResponse> jwtGrantRequest() {
        return webClient.post()
                .uri(tokenEndpointURI)
                .body(fromFormData("grant_type", GrantType.JWT_BEARER.getValue())
                        .with("assertion", jwtBearerGrantGenerator.generate().getAssertion()))
                .retrieve()
                .bodyToMono(JwtBearerGrantResponse.class);
    }
}

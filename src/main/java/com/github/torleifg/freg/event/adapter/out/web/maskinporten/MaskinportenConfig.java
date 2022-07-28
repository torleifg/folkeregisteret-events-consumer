package com.github.torleifg.freg.event.adapter.out.web.maskinporten;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.oauth2.sdk.GeneralException;
import com.nimbusds.oauth2.sdk.as.AuthorizationServerMetadata;
import com.nimbusds.oauth2.sdk.id.Issuer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

@Configuration
public class MaskinportenConfig {
    private final MaskinportenProperties properties;

    public MaskinportenConfig(MaskinportenProperties properties) {
        this.properties = properties;
    }

    @Bean
    public MaskinportenClientDetails maskinportenClientDetails() {
        return new MaskinportenClientDetails(properties.getClientId(), properties.getScope());
    }

    @Bean
    public URI tokenEndpointURI() throws GeneralException, IOException {
        return authorizationServerMetadata().getTokenEndpointURI();
    }

    @Bean
    public Issuer issuer() throws GeneralException, IOException {
        return authorizationServerMetadata().getIssuer();
    }

    @Bean
    public AuthorizationServerMetadata authorizationServerMetadata() throws GeneralException, IOException {
        return AuthorizationServerMetadata.resolve(new Issuer(properties.getWellKnown()), 2000, 30000);
    }

    @Bean
    public JWK jwk() throws IOException, ParseException, JWKException {
        final var inputStream = new ClassPathResource(properties.getJwks()).getInputStream();

        final var jwkSet = JWKSet.parse(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8));
        final var jwk = jwkSet.getKeyByKeyId(properties.getKid());

        if (jwk == null) {
            throw JWKException.expectedClass(JWK.class);
        }

        return jwk;
    }
}

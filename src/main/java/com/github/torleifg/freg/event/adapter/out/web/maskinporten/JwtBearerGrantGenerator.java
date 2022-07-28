package com.github.torleifg.freg.event.adapter.out.web.maskinporten;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.oauth2.sdk.JWTBearerGrant;
import com.nimbusds.oauth2.sdk.id.Issuer;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtBearerGrantGenerator {
    private final Issuer issuer;
    private final JWK jwk;
    private final MaskinportenClientDetails maskinportenClientDetails;

    public JwtBearerGrantGenerator(Issuer issuer, JWK jwk, MaskinportenClientDetails maskinportenClientDetails) {
        this.issuer = issuer;
        this.jwk = jwk;
        this.maskinportenClientDetails = maskinportenClientDetails;
    }

    public JWTBearerGrant generate() {
        final var jwt = new SignedJWT(createJWSHeader(), createJWTClaimsSet());

        try {
            jwt.sign(new RSASSASigner(jwk.toRSAKey()));
        } catch (JOSEException ex) {
            throw new MaskinportenException(ex.getMessage(), ex);
        }

        return new JWTBearerGrant(jwt);
    }

    private JWSHeader createJWSHeader() {
        return new JWSHeader.Builder(JWSAlgorithm.RS256)
                .keyID(jwk.getKeyID())
                .build();
    }

    private JWTClaimsSet createJWTClaimsSet() {
        return new JWTClaimsSet.Builder()
                .audience(issuer.getValue())
                .issuer(maskinportenClientDetails.getClientId())
                .claim("scope", maskinportenClientDetails.getScope())
                .jwtID(UUID.randomUUID().toString())
                .issueTime(new Date(Clock.systemUTC().millis()))
                .expirationTime(new Date(Clock.systemUTC().millis() + 120000))
                .build();
    }
}
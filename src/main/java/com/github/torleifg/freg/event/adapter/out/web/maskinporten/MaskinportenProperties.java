package com.github.torleifg.freg.event.adapter.out.web.maskinporten;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "maskinporten")
public class MaskinportenProperties {
    private String wellKnown;
    private String clientId;
    private String scope;
    private String kid;
    private String jwks;
}
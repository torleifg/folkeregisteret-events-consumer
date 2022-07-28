package com.github.torleifg.freg.event.adapter.out.web.maskinporten;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JwtBearerGrantResponse {
    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonProperty(value = "token_type")
    private String tokenType;

    @JsonProperty(value = "expires_in")
    private int expiresIn;

    @JsonProperty(value = "scope")
    private String scope;
}
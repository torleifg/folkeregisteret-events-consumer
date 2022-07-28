package com.github.torleifg.freg.event.adapter.out.web.freg;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "freg")
public class FregProperties {
    private String baseUrl;
    private String sequence;
    private String feed;
    private String eventDocument;
    private String person;
}

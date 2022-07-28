package com.github.torleifg.freg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class FregApplication {

	public static void main(String[] args) {
		SpringApplication.run(FregApplication.class, args);
	}
}
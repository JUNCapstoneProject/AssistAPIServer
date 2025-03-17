package com.help.stockassistplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class StockAssistPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockAssistPlatformApplication.class, args);
	}
}

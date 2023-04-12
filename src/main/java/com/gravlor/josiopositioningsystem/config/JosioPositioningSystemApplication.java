package com.gravlor.josiopositioningsystem.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_FLOAT_AS_INT;

@SpringBootApplication
@EnableJpaRepositories("com.gravlor.josiopositioningsystem.*")
@ComponentScan(basePackages = { "com.gravlor.josiopositioningsystem.*" })
@EntityScan("com.gravlor.josiopositioningsystem.*")
public class JosioPositioningSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(JosioPositioningSystemApplication.class, args);
	}

	@Bean
	@Primary
	public ObjectMapper buildObjectMapper() {
		return new ObjectMapper().configure(ACCEPT_FLOAT_AS_INT, false);
	}

}

package com.gravlor.josiopositioningsystem.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.gravlor.josiopositioningsystem.*")
@ComponentScan(basePackages = { "com.gravlor.josiopositioningsystem.*" })
@EntityScan("com.gravlor.josiopositioningsystem.*")
public class JosioPositioningSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(JosioPositioningSystemApplication.class, args);
	}

}

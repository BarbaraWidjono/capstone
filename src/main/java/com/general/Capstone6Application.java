package com.general;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//@SpringBootApplication
@Configuration
@ComponentScan(basePackages= {"com.general"})
@EnableAutoConfiguration
public class Capstone6Application {

	public static void main(String[] args) {
		SpringApplication.run(Capstone6Application.class, args);
	}

}


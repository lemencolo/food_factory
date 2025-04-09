package com.food_factory_spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FoodFactorySpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodFactorySpringApplication.class, args);
	}

}

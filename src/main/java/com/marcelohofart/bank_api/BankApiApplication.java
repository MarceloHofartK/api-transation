package com.marcelohofart.bank_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApiApplication.class, args);

		System.out.println("Swagger -> http://localhost:8080/swagger-ui.html");
	}

}

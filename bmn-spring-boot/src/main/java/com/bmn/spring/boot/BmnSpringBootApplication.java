package com.bmn.spring.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.bmn"})
public class BmnSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(BmnSpringBootApplication.class, args);
	}
}

package org.davisr.spring.camel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="org.davisr.spring.camel")
public class SpringCamelTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCamelTestApplication.class, args);
	}
}

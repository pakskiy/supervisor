package com.pakskiy.supervisor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestAuthApplication {

	public static void main(String[] args) {
		SpringApplication.from(AuthApplication::main).with(TestAuthApplication.class).run(args);
	}

}

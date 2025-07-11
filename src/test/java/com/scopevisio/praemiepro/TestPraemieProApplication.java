package com.scopevisio.praemiepro;

import org.springframework.boot.SpringApplication;

public class TestPraemieProApplication {

	public static void main(String[] args) {
		SpringApplication.from(PraemieProApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

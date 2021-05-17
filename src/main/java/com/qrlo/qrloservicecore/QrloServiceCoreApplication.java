package com.qrlo.qrloservicecore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class QrloServiceCoreApplication {
	public static void main(String[] args) {
		SpringApplication.run(QrloServiceCoreApplication.class, args);
	}
}

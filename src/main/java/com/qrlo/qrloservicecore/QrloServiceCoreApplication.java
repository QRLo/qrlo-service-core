package com.qrlo.qrloservicecore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class QrloServiceCoreApplication {
	public static void main(String[] args) {
		// TODO: QRLO-39 (TechDebt | Enable Blockhound to detect blocking call in main thread)
//		BlockHound.install();
		SpringApplication.run(QrloServiceCoreApplication.class, args);
	}
}

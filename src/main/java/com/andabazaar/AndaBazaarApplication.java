package com.andabazaar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients
public class AndaBazaarApplication {
	public static void main(String[] args) {
		SpringApplication.run(AndaBazaarApplication.class, args);
		System.out.println("HEllO ANDA BAZAAR");
	}
}

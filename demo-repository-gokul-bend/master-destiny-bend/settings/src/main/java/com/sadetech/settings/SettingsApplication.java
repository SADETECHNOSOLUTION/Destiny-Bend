package com.sadetech.settings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SettingsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SettingsApplication.class, args);
	}

}

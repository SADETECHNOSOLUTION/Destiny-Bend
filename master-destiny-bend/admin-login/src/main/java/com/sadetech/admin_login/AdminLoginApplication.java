package com.sadetech.admin_login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableDiscoveryClient
@EnableMongoAuditing
public class AdminLoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminLoginApplication.class, args);
	}

}

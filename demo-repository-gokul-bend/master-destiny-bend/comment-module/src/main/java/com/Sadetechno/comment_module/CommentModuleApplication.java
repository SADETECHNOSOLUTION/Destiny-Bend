package com.Sadetechno.comment_module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableMongoAuditing
public class
CommentModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommentModuleApplication.class, args);
	}

//	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//		return builder.sources(CommentModuleApplication.class);
//	}

}

package com.sadetech.websocket_messaging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EnableMongoAuditing
public class WebsocketMessagingApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebsocketMessagingApplication.class, args);
	}

}

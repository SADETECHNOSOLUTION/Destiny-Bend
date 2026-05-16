package com.sadetech.friend_request_module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class FriendRequestModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(FriendRequestModuleApplication.class, args);
	}

}

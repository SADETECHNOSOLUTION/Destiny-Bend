package com.Sadetechno.status_module.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "friend-request-module")
public interface FriendRequestFeignClient {
    @GetMapping("/friend-requests/{userId}/friends")
    Map<String, Object> getFriendListAndCount(@PathVariable Long userId);
}

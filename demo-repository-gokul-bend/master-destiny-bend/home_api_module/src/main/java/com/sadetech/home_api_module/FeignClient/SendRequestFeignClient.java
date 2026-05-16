package com.sadetech.home_api_module.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "friend-request-module", contextId = "send-request")
public interface SendRequestFeignClient {
    @GetMapping("/friend-requests/{userId}/sent-requests")
    Map<String,Object> getSentRequestsDetails(@PathVariable Long userId);
}

package com.sadetech.home_api_module.FeignClient;

import com.sadetech.home_api_module.DTO.FollowResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "follow-module" , contextId = "followerClient")
public interface FollowerFeignClient {
    @GetMapping("/follows/api/followers/{id}")
    FollowResponseDTO getFollowers(@PathVariable Long id);
}

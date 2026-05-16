package com.sadetech.home_api_module.FeignClient;

import com.sadetech.home_api_module.DTO.FollowResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "follow-module", contextId = "followingClient")
public interface FollowingFeignClient {
    @GetMapping("/follows/api/following/{id}")
    FollowResponseDTO getFollowing(@PathVariable Long id);
}

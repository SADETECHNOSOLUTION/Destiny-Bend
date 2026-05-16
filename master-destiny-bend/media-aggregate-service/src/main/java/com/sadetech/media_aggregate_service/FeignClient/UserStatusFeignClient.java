package com.sadetech.media_aggregate_service.FeignClient;

import com.sadetech.media_aggregate_service.DTO.StatusDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "status-module", contextId = "user-status")
public interface UserStatusFeignClient {
    @GetMapping("/statuses/user/{userId}")
    List<StatusDTO> getStatusesByUserId(@PathVariable Long userId);
}

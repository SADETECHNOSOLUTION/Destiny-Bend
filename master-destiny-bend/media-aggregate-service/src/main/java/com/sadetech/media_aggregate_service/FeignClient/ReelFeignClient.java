package com.sadetech.media_aggregate_service.FeignClient;

import com.sadetech.media_aggregate_service.ReelDTO.ReelResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "reels-module",contextId = "getReelById")
public interface ReelFeignClient {
    @GetMapping("/reels/{reelsId}")
    ReelResponse getUserDetailsByReelsId(@PathVariable("reelsId") Long reelsId);
}

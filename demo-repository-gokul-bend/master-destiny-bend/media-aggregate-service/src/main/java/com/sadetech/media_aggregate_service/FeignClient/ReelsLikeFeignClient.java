package com.sadetech.media_aggregate_service.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "like-module",contextId = "reel-like")
public interface ReelsLikeFeignClient {
    @GetMapping("/likes/reels/{reelsId}")
    Map<String,Object> getLikesByReelsId(@PathVariable Long reelsId);
}

package com.sadetech.media_aggregate_service.FeignClient;

import com.sadetech.media_aggregate_service.ReelDTO.ReelResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "reels-module", contextId = "getAll-reel")
public interface AllReelFeignClient {
    @GetMapping("/reels/getAll/reel")
    List<ReelResponse> getAllReels(@RequestParam int page, @RequestParam int size);
}

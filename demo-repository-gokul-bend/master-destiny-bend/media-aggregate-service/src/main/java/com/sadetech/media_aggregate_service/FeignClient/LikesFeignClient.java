package com.sadetech.media_aggregate_service.FeignClient;

import com.sadetech.media_aggregate_service.DTO.LikeStatusDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "like-module", contextId = "status-like")
public interface LikesFeignClient {
    @GetMapping("/likes/status/{statusId}")
    List<LikeStatusDTO> getLikesByStatusId(@PathVariable Long statusId);
}

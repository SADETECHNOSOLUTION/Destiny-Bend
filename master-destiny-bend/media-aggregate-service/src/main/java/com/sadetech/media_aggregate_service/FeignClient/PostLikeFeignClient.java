package com.sadetech.media_aggregate_service.FeignClient;

import com.sadetech.media_aggregate_service.PostDTO.LikeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(name = "like-module",contextId = "post-like")
public interface
PostLikeFeignClient {
    @GetMapping("/likes/post/{postId}")
    Map<String, Object> getLikesByPostId(@PathVariable Long postId);
}

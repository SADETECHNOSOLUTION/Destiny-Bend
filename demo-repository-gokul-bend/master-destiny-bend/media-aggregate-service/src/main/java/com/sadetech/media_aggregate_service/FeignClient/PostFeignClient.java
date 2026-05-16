package com.sadetech.media_aggregate_service.FeignClient;

import com.sadetech.media_aggregate_service.PostDTO.PostResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "post-module",contextId = "get-post")
public interface PostFeignClient {
    @GetMapping("/posts/{postId}")
    PostResponseDTO getPostWithUserDetails(@PathVariable Long postId);
}

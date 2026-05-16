package com.sadetech.media_aggregate_service.FeignClient;

import com.sadetech.media_aggregate_service.PostDTO.PostResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "post-module",contextId = "get-allPost")
public interface AllPostFeignClient {
    @GetMapping("/posts/all")
    List<PostResponseDTO> getAllPosts(@RequestParam int page, @RequestParam int size);
}


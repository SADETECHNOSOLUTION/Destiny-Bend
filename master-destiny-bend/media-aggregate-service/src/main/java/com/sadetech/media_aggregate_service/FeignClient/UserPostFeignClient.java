package com.sadetech.media_aggregate_service.FeignClient;

import com.sadetech.media_aggregate_service.PostDTO.PostResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "post-module", contextId = "get-all-post-user")
public interface UserPostFeignClient {

    @GetMapping("/posts/user/{userId}")
    List<PostResponseDTO> getPostsByUserId(@PathVariable Long userId);
}

package com.Sadetechno.like_module.FeignClient;

import com.Sadetechno.like_module.DTO.PostDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "post-module")
public interface PostFeignClient {
    @GetMapping("/posts/{postId}")
    PostDTO getPostWithUserDetails(@PathVariable("postId") Long postId);
}

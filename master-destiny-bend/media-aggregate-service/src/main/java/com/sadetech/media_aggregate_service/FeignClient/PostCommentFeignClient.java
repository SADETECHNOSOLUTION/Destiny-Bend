package com.sadetech.media_aggregate_service.FeignClient;

import com.sadetech.media_aggregate_service.PostDTO.CommentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "comment-module", contextId = "post-comment")
public interface PostCommentFeignClient {
    @GetMapping("/comments/post/{postId}")
    List<CommentResponse> getCommentsByPostId(@PathVariable Long postId);
}

package com.Sadetechno.like_module.FeignClient;

import com.Sadetechno.like_module.DTO.CommentLikeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "comment-module", contextId = "postComment")
public interface CommentPostFeignClient {
    @GetMapping("/comments/get-user/post/{id}")
    CommentLikeDTO getUserDetailsByCommentIdPost(@PathVariable String id);
}

package com.Sadetechno.comment_module.FeignClient;

import com.Sadetechno.comment_module.DTO.CommentLike;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "like-module")
public interface LikeFeignClient {
    @GetMapping("/likes/comments/{commentId}")
    List<CommentLike> getLikesByCommentId(@PathVariable String commentId);
}

package com.sadetech.media_aggregate_service.FeignClient;

import com.sadetech.media_aggregate_service.ReelDTO.CommentReelsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "comment-module",contextId = "reel-comment")
public interface ReelCommentFeignClient {
    @GetMapping("/comments/comment-reels/{reelsId}")
    List<CommentReelsResponse> getCommentForReels(@PathVariable Long reelsId);
}

package com.sadetech.media_aggregate_service.FeignClient;

import com.sadetech.media_aggregate_service.DTO.CommentStatusDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "comment-module",contextId = "status-comment")
public interface CommentFeignClient {
    @GetMapping("/comments/get-status/{statusId}")
    List<CommentStatusDTO> getComments(@PathVariable Long statusId);
}

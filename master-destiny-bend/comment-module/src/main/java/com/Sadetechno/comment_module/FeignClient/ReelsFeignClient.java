package com.Sadetechno.comment_module.FeignClient;

import com.Sadetechno.comment_module.DTO.ReelsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "reels-module")
public interface ReelsFeignClient {
    @GetMapping("/reels/{id}")
    ReelsDTO getUserDetailsByReelsId(@PathVariable("id") Long id);
}

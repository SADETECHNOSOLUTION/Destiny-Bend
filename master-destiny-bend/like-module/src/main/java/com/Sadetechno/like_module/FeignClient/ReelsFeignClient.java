package com.Sadetechno.like_module.FeignClient;

import com.Sadetechno.like_module.DTO.ReelsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "reels-module")
public interface ReelsFeignClient {
    @GetMapping("/reels/{id}")
    ReelsDTO getUserDetailsByReelsId(@PathVariable("id") Long id);
}

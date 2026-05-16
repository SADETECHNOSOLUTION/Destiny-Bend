package com.Sadetechno.like_module.FeignClient;

import com.Sadetechno.like_module.DTO.StatusDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "status-module")
public interface StatusFeignClient {
    @GetMapping("/statuses/{id}")
    StatusDTO getUserDetailsByStatusId(@PathVariable("id") Long id);
}

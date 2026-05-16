package com.Sadetechno.comment_module.FeignClient;

import com.Sadetechno.comment_module.DTO.StatusDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "status-module")
public interface StatusFeignClient {
    @GetMapping("/statuses/{id}")
    StatusDTO getUserDetailsByStatusId(@PathVariable("id") Long id);
}

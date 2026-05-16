package com.Sadetechno.post_module.FeignClient;

import com.Sadetechno.post_module.DTO.ResponseDTO;
import com.Sadetechno.post_module.model.Post;
import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-module")
public interface UserFeignClient {

    @GetMapping("/api/users/{id}")
    ResponseDTO getUserById(@PathVariable("id") Long id) throws FeignException;
}
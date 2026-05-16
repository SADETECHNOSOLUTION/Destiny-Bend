package com.sadetech.friend_request_module.FeignConnect;

import com.sadetech.friend_request_module.Dto.UserDTO;
import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-module")
public interface UserFeignClient {
    @GetMapping("/api/users/{id}")
    UserDTO getUserById(@PathVariable("id") Long id) throws FeignException;
}


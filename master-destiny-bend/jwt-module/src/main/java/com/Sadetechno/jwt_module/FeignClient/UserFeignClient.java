package com.Sadetechno.jwt_module.FeignClient;

import com.Sadetechno.jwt_module.DTO.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-module")
public interface UserFeignClient {

    @PutMapping("/api/users/{userid}/updateContact")
    UserDTO updateEmailAndPhoneNumber(
            @PathVariable Long userid,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber);
}

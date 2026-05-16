package com.sadetech.notification_module.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "follow-module")
public interface FollowFeignClient {
    @DeleteMapping("/follows/notification/{id}/{type}")
    void deleteNotification(@PathVariable String id, @PathVariable String type);
}

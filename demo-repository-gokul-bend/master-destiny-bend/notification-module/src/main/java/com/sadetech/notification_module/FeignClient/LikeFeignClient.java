package com.sadetech.notification_module.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "like-module")
public interface LikeFeignClient {
    @DeleteMapping("/likes/notification/{id}/{type}")
    void deleteNotification(@PathVariable String id, @PathVariable String type);
}

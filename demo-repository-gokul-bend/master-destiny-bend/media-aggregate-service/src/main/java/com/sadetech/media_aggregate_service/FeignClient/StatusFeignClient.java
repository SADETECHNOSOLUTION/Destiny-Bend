package com.sadetech.media_aggregate_service.FeignClient;

import com.sadetech.media_aggregate_service.DTO.StatusDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "status-module")
public interface StatusFeignClient {
    @GetMapping("/statuses/{statusId}")
    StatusDTO getUserDetailsByStatusId(@PathVariable("statusId") Long statusId);
}

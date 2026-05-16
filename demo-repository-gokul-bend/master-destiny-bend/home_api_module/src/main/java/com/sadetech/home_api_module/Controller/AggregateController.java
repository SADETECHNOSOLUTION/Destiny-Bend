package com.sadetech.home_api_module.Controller;

import com.sadetech.home_api_module.DTO.AggregateResponseDTO;
import com.sadetech.home_api_module.Service.AggregateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home/api")
public class AggregateController {
    @Autowired
    private AggregateService aggregateService;

    @GetMapping("/aggregate/{userId}")
    public AggregateResponseDTO getAggregateData(@PathVariable Long userId) {
        return aggregateService.getAggregateData(userId);
    }
}

package com.sadetech.Actuator.Service;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class ServiceHealthMonitor {

    private final EmailService emailService;
    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;

    public ServiceHealthMonitor(EmailService emailService, RestTemplate restTemplate, DiscoveryClient discoveryClient) {
        this.emailService = emailService;
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
    }

    @Scheduled(fixedRate = 60000                      ) // Check every minute
    public void monitorHealth() {
        List<String> services = discoveryClient.getServices(); // Fetch all registered services
        for (String service : services) {
            List<String> instances = discoveryClient.getInstances(service).stream()
                    .map(instance -> instance.getUri().toString() + "/actuator/health")
                    .toList();

            for (String healthUrl : instances) {
                try {
                    String healthStatus = restTemplate.getForObject(healthUrl, String.class);
                    if (healthStatus != null && healthStatus.contains("\"status\":\"DOWN\"")) {
                        emailService.sendEmail("Service Down", "Service at " + healthUrl + " is currently down.");
                    }
                } catch (Exception e) {
                    emailService.sendEmail("Service Unreachable", "Service at " + healthUrl + " is currently unreachable.");
                }
            }
        }
    }
}



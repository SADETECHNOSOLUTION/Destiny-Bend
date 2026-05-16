package com.sadetech.notification_module.Controller;

import com.sadetech.notification_module.Service.NotificationAggregationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationAggregationService notificationAggregationService;

    @DeleteMapping("/{id}/{type}")
    public ResponseEntity<String> deleteNotification(@PathVariable String id, @PathVariable String type) {
        notificationAggregationService.deleteNotification(id, type);
        return ResponseEntity.ok("Notification deleted");
    }
}

package com.example.follow_module.Controller;

import com.example.follow_module.DTO.FollowResponseDTO;
import com.example.follow_module.DTO.NotificationDTO;
import com.example.follow_module.Model.Notification;
import com.example.follow_module.Repository.NotificationRepository;
import com.example.follow_module.Service.FollowService;
import com.example.follow_module.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/follows")
public class FollowController {

    @Autowired
    private FollowService followService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @PostMapping("/follow/{followerId}/{followingId}")
    public ResponseEntity<String> followUser(@PathVariable Long followerId, @PathVariable Long followingId) {
        try {
            followService.followUser(followerId, followingId);
            return ResponseEntity.ok("Followed successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/unfollow/{followerId}/{followingId}")
    public ResponseEntity<String> unfollowUser(@PathVariable Long followerId, @PathVariable Long followingId) {
        try {
            followService.unfollowUser(followerId, followingId);
            return ResponseEntity.ok("Unfollowed successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/api/followers/{id}")
    public FollowResponseDTO getFollowers(@PathVariable Long id) {
        return followService.getFollowers(id);
    }

    @GetMapping("/api/following/{id}")
    public FollowResponseDTO getFollowing(@PathVariable Long id) {
        return followService.getFollowing(id);
    }

    @GetMapping("/notifications/{userId}")
    public NotificationDTO getNotificationsForUser(@PathVariable Long userId) {
        List<Notification> notifications = notificationRepository.findAll().stream()
                .filter(notification -> notification.getUserId().equals(userId))
                .collect(Collectors.toList());

        int count =  notifications.size();
        return new NotificationDTO(notifications,count);
    }

    @DeleteMapping("/notification/{id}/{type}")
    public ResponseEntity<String> deleteNotification(@PathVariable Long id, @PathVariable String type){
        notificationService.deleteNotificationByIdAndType(id, type);
        return ResponseEntity.ok("Notification Deleted");
    }

    @DeleteMapping("/notification/delete-all")
    public ResponseEntity<String> deleteAllNotification(){
        List<Notification> notifications = notificationRepository.findAll();
        if(!notifications.isEmpty()){
            notificationRepository.deleteAll();
        }else {
            throw new IllegalArgumentException("No notification found.");
        }
        return ResponseEntity.ok("Notification deleted.");
    }
}


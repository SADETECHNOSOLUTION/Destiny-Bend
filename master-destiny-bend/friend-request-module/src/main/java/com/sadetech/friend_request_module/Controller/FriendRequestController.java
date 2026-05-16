package com.sadetech.friend_request_module.Controller;

import com.sadetech.friend_request_module.Dto.NotificationDTO;
import com.sadetech.friend_request_module.Dto.UserDTO;
import com.sadetech.friend_request_module.Model.FriendRequest;
import com.sadetech.friend_request_module.Model.Notification;
import com.sadetech.friend_request_module.Repository.NotificationRepository;
import com.sadetech.friend_request_module.Service.FriendRequestService;
import com.sadetech.friend_request_module.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/friend-requests")
public class FriendRequestController {

    @Autowired
    private FriendRequestService friendRequestService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @PostMapping("/send")
    public ResponseEntity<String> sendFriendRequest(@RequestParam Long senderId, @RequestParam Long recipientId) {
            FriendRequest friendRequest = friendRequestService.makeRequest(senderId, recipientId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Friend request sent successfully.");
    }

    @PostMapping("/accept")
    public ResponseEntity<String> acceptFriendRequest(@RequestParam Long senderId, @RequestParam Long recipientId) {
            friendRequestService.acceptFriendRequest(senderId, recipientId);
            return ResponseEntity.ok("Friend request accepted.");
    }


    @PostMapping("/decline")
    public ResponseEntity<String> declineFriendRequest(@RequestParam Long senderId, @RequestParam Long recipientId) {
            friendRequestService.declineFriendRequest(senderId, recipientId);
            return new ResponseEntity<>("Friend request declined.", HttpStatus.OK);
    }

    @DeleteMapping("/delete-friend/{senderId}/{recipientId}")
    public ResponseEntity<String> deleteFriend(@PathVariable Long senderId, @PathVariable Long recipientId) {
            friendRequestService.deleteFriend(senderId, recipientId);
            return ResponseEntity.ok("User has been removed from friend list.");
    }

    // Endpoint to get the list of pending friend requests (requests sent to the user)
    @GetMapping("/{userId}/pending-requests")
    public ResponseEntity<Map<String, Object>> getPendingRequestsDetails(@PathVariable Long userId) {
        Map<String, Object> response = friendRequestService.getPendingRequestsDetails(userId);
        return ResponseEntity.ok(response);
    }

    // Endpoint to get the list of pending friend requests sent by the user
    @GetMapping("/{userId}/sent-requests")
    public ResponseEntity<Map<String,Object>> getSentRequestsDetails(@PathVariable Long userId) {
       Map<String, Object> response = friendRequestService.getSentRequestsDetails(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId1}/mutual-friends/{userId2}")
    public ResponseEntity<List<UserDTO>> getMutualFriends(@PathVariable Long userId1, @PathVariable Long userId2) {
            List<UserDTO> mutualFriends = friendRequestService.getMutualFriends(userId1, userId2);
            return ResponseEntity.ok(mutualFriends);
    }

    @GetMapping("/notifications/{userId}")
    public NotificationDTO getNotificationsForUser(@PathVariable Long userId) {
        List<Notification> notifications = notificationRepository.findAll().stream()
                .filter(notification -> notification.getUserId().equals(userId))
                .collect(Collectors.toList());

        int count = notifications.size();
        return new NotificationDTO(notifications,count);
    }

    @DeleteMapping("/notification/{id}/{type}")
    public ResponseEntity<String> deleteNotification(@PathVariable Long id, @PathVariable String type){
       notificationService.deleteNotificationForFriendRequest(id,type);
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

    @GetMapping("/{userId}/friends")
    public ResponseEntity<Map<String, Object>> getFriendListAndCount(@PathVariable Long userId) {
        Map<String, Object> response = friendRequestService.getFriendListAndCount(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/friend-list/{requestedUserId}")
    public ResponseEntity<?> getFriendListByVisibility(
            @RequestParam Long userId,
            @PathVariable("requestedUserId") Long requestedUserId) {
        try {
            // Fetch the friend list based on visibility rules
            Map<String, Object> friendList = friendRequestService.getFriendListByVisibility(userId, requestedUserId);

            return ResponseEntity.ok(friendList);
        } catch (IllegalArgumentException e) {
            // If access is denied due to privacy settings, return a 403 (Forbidden) response
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            // Handle other exceptions and return a 500 (Internal Server Error)
            return ResponseEntity.status(500).body("An error occurred while fetching the friend list.");
        }
    }

}
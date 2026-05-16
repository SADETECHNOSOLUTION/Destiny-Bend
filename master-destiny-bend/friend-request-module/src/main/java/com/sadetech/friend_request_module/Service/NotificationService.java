package com.sadetech.friend_request_module.Service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.sadetech.friend_request_module.Model.Notification;
import com.sadetech.friend_request_module.Repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void createNotification(Long userId, String message, String type, String email,String profileImagePath) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setType(type);
        notification.setEmail(email);
        notification.setProfileImagePath(profileImagePath);
        notificationRepository.save(notification);
    }

    @Transactional
    public void deleteNotificationForFriendRequest(Long id, String type) {
        Optional<Notification> deleteNotification = notificationRepository.findById(id);

        if (deleteNotification.isPresent()){
            notificationRepository.deleteByIdAndType(id, type);
        }else {
            throw new IllegalArgumentException("ID not found or type mismatch.");
        }
    }
}
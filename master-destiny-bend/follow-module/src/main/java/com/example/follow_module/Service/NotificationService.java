package com.example.follow_module.Service;

import com.example.follow_module.Model.Notification;
import com.example.follow_module.Repository.NotificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void createNotification(Long userId, String message, String email,String profileImagePath,String type){
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setEmail(email);
        notification.setProfileImagePath(profileImagePath);
        notification.setType(type);
        notificationRepository.save(notification);
    }


    @Transactional
    public void deleteNotificationByIdAndType(Long id, String type) {
        Optional<Notification> deleteNotificationForFollow = notificationRepository.findById(id);

        if(deleteNotificationForFollow.isPresent()){
            notificationRepository.deleteByIdAndType(id, type);
        }else {
            throw new IllegalArgumentException("Id not found or type mismatch.");
        }
    }
}

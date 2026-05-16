package com.Sadetechno.comment_module.Service;

import com.Sadetechno.comment_module.Repository.ReelsRepository;
import com.Sadetechno.comment_module.model.PostNotification;
import com.Sadetechno.comment_module.model.ReelsNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReelsNotificationService {

    @Autowired
    private ReelsRepository reelsRepository;

    public void createNotificationForReels(Long userId, String message, String email, String type, Long reelsId, String name, String profileImagePath,Long reelsOwnerId) {
        ReelsNotification notification = new ReelsNotification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setEmail(email);
        notification.setType(type);
        notification.setReelsId(reelsId);
        notification.setName(name);
        notification.setProfileImagePath(profileImagePath);
        notification.setReelsOwnerId(reelsOwnerId);

        reelsRepository.save(notification);

    }

//    @Transactional
    public void deleteNotificationForReels(String id, String type) {
        Optional<ReelsNotification> deleteReelsNotification = reelsRepository.findById(id);

        if(deleteReelsNotification.isPresent()){
            reelsRepository.deleteByIdAndType(id,type);
        }else {
            throw new IllegalArgumentException("No id found for Reels notification");
        }
    }
}

package com.Sadetechno.like_module.Service;

import com.Sadetechno.like_module.Repository.ReelsNotificationRepository;
import com.Sadetechno.like_module.model.ReelsNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReelsNotificationService {

    @Autowired
    private ReelsNotificationRepository reelsNotificationRepository;

    public void createNotificationForReels(Long userId, String message, String email, String type,Long reelsId,String name, String profileImagePath,Long reelsOwnerId){
        ReelsNotification reelsNotification = new ReelsNotification();
        reelsNotification.setUserId(userId);
        reelsNotification.setMessage(message);
        reelsNotification.setEmail(email);
        reelsNotification.setType(type);
        reelsNotification.setReelsId(reelsId);
        reelsNotification.setName(name);
        reelsNotification.setProfileImagePath(profileImagePath);
        reelsNotification.setReelsOwnerId(reelsOwnerId);
        reelsNotificationRepository.save(reelsNotification);
    }

    public void deleteNotificationForReels(String id,String type){
        Optional<ReelsNotification> deleteReelsNotification = reelsNotificationRepository.findById(id);
        if(deleteReelsNotification.isPresent()){
            reelsNotificationRepository.deleteByIdAndType(id,type);
        }else {
            throw new IllegalArgumentException("No id found for Reels notification");
        }
    }
}

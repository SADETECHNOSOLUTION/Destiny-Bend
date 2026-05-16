package com.Sadetechno.like_module.Service;

import com.Sadetechno.like_module.Repository.CommentLikeNotificationRepository;
import com.Sadetechno.like_module.model.CommentLikeNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentLikeNotificationService {

    @Autowired
    private CommentLikeNotificationRepository commentLikeNotificationRepository;

    public void createNotificationForCommentLike(Long userId, String message, String email, String type,String commentId,String name, String profileImagePath,Long commentOwnerId) {
        CommentLikeNotification notification = new CommentLikeNotification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setEmail(email);
        notification.setType(type);
        notification.setCommentId(commentId);
        notification.setName(name);
        notification.setProfileImagePath(profileImagePath);
        notification.setCommentOwnerId(commentOwnerId);
        commentLikeNotificationRepository.save(notification);
    }
    // Additional methods to retrieve notifications can be added here

    public void deleteNotificationForComment(String id,String type){
        Optional<CommentLikeNotification> deleteCommentNotification = commentLikeNotificationRepository.findById(id);
        if(deleteCommentNotification.isPresent()){
            commentLikeNotificationRepository.deleteByIdAndType(id,type);
        }else {
            throw new IllegalArgumentException("No id found for Post notification");
        }
    }
}

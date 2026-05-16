package com.Sadetechno.comment_module.Service;

import com.Sadetechno.comment_module.Repository.PostRepository;
import com.Sadetechno.comment_module.model.PostNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostNotificationService {

    @Autowired
    private PostRepository postRepository;

    public void createNotification(Long userId, String message, String email, String type, Long postId, String name, String profileImagePath, Long repliedToUserId,Long postOwnerId) {
        PostNotification notification = new PostNotification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setEmail(email);
        notification.setType(type);
        notification.setPostId(postId);
        notification.setName(name);
        notification.setProfileImagePath(profileImagePath);
        notification.setRepliedToUserId(repliedToUserId);
        notification.setPostOwnerId(postOwnerId);

        postRepository.save(notification);

        // Trigger real-time notification logic (e.g., through OneSignal, WebSocket)
        // Example (OneSignal trigger):
        // oneSignalService.sendNotification(userId, message, postId, type);


    }

//    @Transactional
    public void deleteNotificationForPost(String id, String type) {
        Optional<PostNotification> deletePostNotification = postRepository.findById(id);

        if(deletePostNotification.isPresent()){
            postRepository.deleteByIdAndType(id,type);
        }else {
            throw new IllegalArgumentException("No id found for Post notification");
        }
    }
}


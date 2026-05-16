package com.Sadetechno.like_module.Service;

import com.Sadetechno.like_module.DTO.UserDTO;
import com.Sadetechno.like_module.FeignClient.PostFeignClient;
import com.Sadetechno.like_module.FeignClient.UserFeignClient;
import com.Sadetechno.like_module.Repository.LikeRepository;
import com.Sadetechno.like_module.model.Like;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostNotificationService postNotificationService;

    private static final Logger logger = LoggerFactory.getLogger(LikeService.class);

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private PostFeignClient postFeignClient;

    public Like toggleLike(Long postId, Long userId) {
        // Check if the user has already liked the post
        Optional<Like> existingLike = likeRepository.findByPostIdAndUserId(postId, userId);

        if (existingLike.isPresent()) {
            // If a like exists, remove it
            likeRepository.delete(existingLike.get());
            return null; // Indicate that the like was removed
        } else {
            // If no like exists, add a new like
            Like like = new Like();
            like.setPostId(postId);
            like.setUserId(userId);
            UserDTO userDetail = userFeignClient.getUserById(userId);
            String profileImagePath = userDetail.getProfileImagePath();
            String userEmail = userDetail.getEmail();  // Get the email
            String userName =  userFeignClient.getUserById(userId).getName();

            Long postOwnerId = postFeignClient.getPostWithUserDetails(postId).getUserId();
            logger.info("Has found post owner id {} of post id {}",postOwnerId,postId);

            Like savedLike = likeRepository.save(like);
           if(!userId.equals(postOwnerId)){
               String notificationMessage = "liked your post.";
               postNotificationService.createNotification(userId, notificationMessage, userEmail, "POST-LIKE", postId, userName, profileImagePath, postOwnerId);
           }
           return savedLike;
        }
    }

    public Map<String,Object> getLikesByPostId(Long postId) {
        Long count = likeRepository.countByPostId(postId);
        List<Like> like = likeRepository.findByPostId(postId);
        Map<String, Object> response = new HashMap<>();
        response.put("likes",like);
        response.put("likesCount",count);
        return response;
    }

    public long getLikeCountByPostId(Long postId) {
        return likeRepository.countByPostId(postId);
    }
    public List<Long> getUserIdsWhoLikedPost(Long postId) {
        return likeRepository.findUserIdByPostId(postId);
    }

    public boolean hasUserLikedPost(Long postId, Long userId) {
        Optional<Like> existingLike = likeRepository.findByPostIdAndUserId(postId, userId);
        return existingLike.isPresent();
    }

    public List<Like> getLikesByPostIds(Long postId) {
        return likeRepository.findByPostId(postId);
    }

    public List<Like> getAllLikeForPost(){
        return likeRepository.findAll();
    }

}

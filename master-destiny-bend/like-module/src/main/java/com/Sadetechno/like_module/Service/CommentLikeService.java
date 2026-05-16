package com.Sadetechno.like_module.Service;

import com.Sadetechno.like_module.DTO.CommentLikeDTO;
import com.Sadetechno.like_module.DTO.UserDTO;
import com.Sadetechno.like_module.FeignClient.CommentPostFeignClient;
import com.Sadetechno.like_module.FeignClient.CommentReelsFeignClient;
import com.Sadetechno.like_module.FeignClient.UserFeignClient;
import com.Sadetechno.like_module.Repository.CommentLikeRepository;
import com.Sadetechno.like_module.model.CommentLike;
import com.Sadetechno.like_module.model.CommentLikeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentLikeService {

    @Autowired
    private CommentLikeRepository commentLikeRepository;

    @Autowired
    private CommentLikeNotificationService commentLikeNotificationService;

    @Autowired
    private CommentPostFeignClient commentPostFeignClient;

    @Autowired
    private CommentReelsFeignClient commentReelsFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    private static final Logger logger = LoggerFactory.getLogger(CommentLikeService.class);

    public CommentLike toggleLikeForComment(String commentId, Long userId, CommentLikeType commentLikeType) {

        // Check if the like already exists, and if so, remove it
        Optional<CommentLike> commentLike = commentLikeRepository.findByCommentIdAndUserId(commentId, userId);
        if (commentLike.isPresent()) {
            commentLikeRepository.delete(commentLike.get());
            return null;
        } else {
            // Create a new CommentLike object if none exists
            CommentLike newCommentLike = new CommentLike();
            newCommentLike.setCommentId(commentId);
            newCommentLike.setUserId(userId);
            newCommentLike.setCommentLikeType(commentLikeType);

            // Fetch user details with error handling
            UserDTO userDTO = null;
            try {
                userDTO = userFeignClient.getUserById(userId);
                logger.info("UserDTO Response: {}", userDTO);
            } catch (Exception e) {
                logger.error("Failed to fetch user data for userId {}: {}", userId, e.getMessage());
                // Optional: return null or handle as needed if user data is critical
            }

            // If userDTO is null, you may want to handle this scenario (e.g., skip notification creation)
            if (userDTO != null) {
                String userName = userDTO.getName();
                String profileImagePath = userDTO.getProfileImagePath();
                String email = userDTO.getEmail();

                // Determine the like type and handle accordingly
                try {
                    if (commentLikeType == CommentLikeType.POST_COMMENT_LIKE) {
                        CommentLikeDTO commentLikeDTO = commentPostFeignClient.getUserDetailsByCommentIdPost(commentId);
                        logger.info("CommentLikeDTO (Post) Response: {}", commentLikeDTO);
                        Long commentOwnerId = commentLikeDTO.getUserId();

                        if (!userId.equals(commentOwnerId)) {
                            String notificationMessage = "liked your comment.";
                            commentLikeNotificationService.createNotificationForCommentLike(
                                    userId, notificationMessage, email, "LIKE_COMMENT", commentId, userName, profileImagePath, commentOwnerId);
                        }
                    } else if (commentLikeType == CommentLikeType.REELS_COMMENT_LIKE) {
                        CommentLikeDTO commentLikeDTO = commentReelsFeignClient.getUserDetailsByCommentIdReels(commentId);
                        logger.info("CommentLikeDTO (Reels) Response: {}", commentLikeDTO);
                        Long commentOwnerId = commentLikeDTO.getUserId();

                        if (!userId.equals(commentOwnerId)) {
                            String notificationMessage = "liked your comment.";
                            commentLikeNotificationService.createNotificationForCommentLike(
                                    userId, notificationMessage, email, "LIKE_COMMENT", commentId, userName, profileImagePath, commentOwnerId);
                        }
                    }
                } catch (Exception e) {
                    logger.error("Failed to fetch comment details or create notification for commentId {}: {}", commentId, e.getMessage());
                }
            }

            // Save the new like
            return commentLikeRepository.save(newCommentLike);
        }
    }


    public List<CommentLike> getLikesByCommentId(String commentId) {
        return commentLikeRepository.findByCommentId(commentId);
    }

    public long getLikeCountByCommentId(String commentId) {
        return commentLikeRepository.countByCommentId(commentId);
    }
    public List<Long> getUserIdsWhoLikedComment(String commentId) {
        return commentLikeRepository.findUserIdByCommentId(commentId);
    }

    public boolean hasUserLikedComment(String commentId, Long userId) {
        Optional<CommentLike> existingLike = commentLikeRepository.findByCommentIdAndUserId(commentId, userId);
        return existingLike.isPresent();
    }
}

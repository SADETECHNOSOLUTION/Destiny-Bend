package com.Sadetechno.like_module.Controller;
import com.Sadetechno.like_module.DTO.CommentLikeNotificationDTO;
import com.Sadetechno.like_module.DTO.PostNotificationDTO;
import com.Sadetechno.like_module.DTO.ReelsNotificationDTO;
import com.Sadetechno.like_module.DTO.StatusNotificationDTO;
import com.Sadetechno.like_module.Repository.*;
import com.Sadetechno.like_module.Service.*;
import com.Sadetechno.like_module.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private LikeStatusService likeStatusService;

    @Autowired
    private LikeReelsService likeReelsService;

    @Autowired
    private PostNotificationService postNotificationService;

    @Autowired
    private ReelsNotificationService reelsNotificationService;

    @Autowired
    private StatusNotificationService statusNotificationService;

    @Autowired
    private PostNotificationRepository postNotificationRepository;

    @Autowired
    private StatusNotificationRepository statusNotificationRepository;

    @Autowired
    private ReelsNotificationRepository reelsNotificationRepository;

    @Autowired
    private CommentLikeService commentLikeService;

    @Autowired
    private CommentLikeNotificationService commentLikeNotificationService;

    @Autowired
    private CommentLikeNotificationRepository commentLikeNotificationRepository;

    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);

    @PostMapping("/toggle")
    public ResponseEntity<?> toggleLike(
            @RequestParam("postId") Long postId,
            @RequestParam("userId") Long userId) {

        Like like = likeService.toggleLike(postId, userId);
        if (like == null) {
            return new ResponseEntity<>("Like removed", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(like, HttpStatus.CREATED);
        }
    }

    @PostMapping("/toggle-comment")
    public ResponseEntity<?> toggleLikeForComment(
            @RequestParam String commentId,
            @RequestParam Long userId,
            @RequestParam CommentLikeType commentLikeType) {

        CommentLike like = commentLikeService.toggleLikeForComment(commentId, userId, commentLikeType);

        if (like == null) {
            return new ResponseEntity<>("Like removed", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(like, HttpStatus.CREATED);
        }
    }


    @PostMapping("/toggle-status")
    public ResponseEntity<?> toggleLikeForStatus(
            @RequestParam("statusId") Long statusId,
            @RequestParam("userId") Long userId) {

        LikeStatus likeStatus = likeStatusService.toggleLikeStatus(statusId, userId);
        if (likeStatus == null) {
            return new ResponseEntity<>("Like removed", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(likeStatus, HttpStatus.CREATED);
        }
    }
    @PostMapping("/toggle-reels")
    public ResponseEntity<?> toggleLikeForReels(
            @RequestParam("reelsId") Long reelsId,
            @RequestParam("userId") Long userId) {

        LikeReels likeReels = likeReelsService.toggleLikeReels(reelsId, userId);
        if (likeReels == null) {
            return new ResponseEntity<>("Like removed", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(likeReels, HttpStatus.CREATED);
        }
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<Map<String, Object>> getLikesByPostId(@PathVariable Long postId) {
        Map<String, Object> response = likeService.getLikesByPostId(postId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/post/like/{postId}")
    public ResponseEntity<List<Like>> getLikesByPostIds(@PathVariable Long postId) {
        List<Like> likes = likeService.getLikesByPostIds(postId);
        return new ResponseEntity<>(likes, HttpStatus.OK);
    }

    @GetMapping("/reels/{reelsId}")
    public ResponseEntity<Map<String,Object>> getLikesByReelsId(@PathVariable Long reelsId){
        Map<String,Object> likes = likeReelsService.getLikesByReelsId(reelsId);
        return new ResponseEntity<>(likes,HttpStatus.OK);
    }

    @GetMapping("/reels/like/{reelsId}")
    public ResponseEntity<List<LikeReels>> getLikesByReelsIds(@PathVariable Long reelsId){
        List<LikeReels> likes = likeReelsService.getLikesByReelsIds(reelsId);
            return new ResponseEntity<>(likes,HttpStatus.OK);
        }

    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<LikeStatus>> getLikesByStatusId(@PathVariable Long statusId){
        List<LikeStatus> likes = likeStatusService.getLikesByStatusId(statusId);
        return new ResponseEntity<>(likes,HttpStatus.OK);
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<List<CommentLike>> getLikesByCommentId(@PathVariable String commentId){
        List<CommentLike> likes = commentLikeService.getLikesByCommentId(commentId);
        return new ResponseEntity<>(likes,HttpStatus.OK);
    }

    @GetMapping("/post/{postId}/count")
    public ResponseEntity<Long> getLikeCountByPostId(@PathVariable Long postId) {
        long count = likeService.getLikeCountByPostId(postId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/reels/{reelsId}/count")
    public ResponseEntity<Long> getLikesCountByReelsId(@PathVariable Long reelsId){
        long count = likeReelsService.getLikeCountByReelsId(reelsId);
        return new ResponseEntity<>(count,HttpStatus.OK);
    }

    @GetMapping("/status/{statusId}/count")
    public ResponseEntity<Long> getLikesCountByStatusId(@PathVariable Long statusId){
        long count = likeStatusService.getLikeCountByStatusId(statusId);
        return new ResponseEntity<>(count,HttpStatus.OK);
    }

    @GetMapping("/comments/{commentId}/count")
    public ResponseEntity<Long> getLikesCountByCommentId(@PathVariable String commentId){
        long count = commentLikeService.getLikeCountByCommentId(commentId);
        return new ResponseEntity<>(count,HttpStatus.OK);
    }

    { /*

    @GetMapping("/post/{postId}/users")
    public ResponseEntity<List<Long>> getUsersWhoLikedPost(@PathVariable Long postId) {
        try {
            List<Long> userIds = likeService.getUserIdsWhoLikedPost(postId);
            logger.info("List of users who liked the post {}",userIds);
            return new ResponseEntity<>(userIds, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getting user ids is {}",e.getMessage());
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/reels/{reelsId}/users")
    public ResponseEntity<List<Long>> getUserIdsWhoLikedReels(@PathVariable Long reelsId){
        try {
            List<Long> userIds = likeReelsService.getUserIdsWhoLikedReels(reelsId);
            logger.info("List of users who liked the reels {}",userIds);
            return new ResponseEntity<>(userIds, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getting user ids of reels {}",e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/status/{statusId}/users")
    public ResponseEntity<List<Long>> getUserIdsWhoLikedStatus(@PathVariable Long statusId){
        try {
            List<Long> userIds = likeStatusService.getUserIdsWhoLikedStatus(statusId);
            logger.info("List of users who liked the status {}",userIds);
            return new ResponseEntity<>(userIds, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getting user ids of status {}",e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/comments/{commentId}/users")
    public ResponseEntity<List<Long>> getUserIdsWhoLikedComments(@PathVariable String commentId){
        try {
            List<Long> userIds = commentLikeService.getUserIdsWhoLikedComment(commentId);
            logger.info("List of users who liked the comments {}",userIds);
            return new ResponseEntity<>(userIds, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getting user ids of comment {}",e.getMessage());
            throw new RuntimeException(e);
        }
    }
    */ }

    @GetMapping("/post/{postId}/user/{userId}")
    public ResponseEntity<Boolean> hasUserLikedPost(
            @PathVariable Long postId,
            @PathVariable Long userId) {

        boolean hasLiked = likeService.hasUserLikedPost(postId, userId);
        return new ResponseEntity<>(hasLiked, HttpStatus.OK);
    }

    @GetMapping("/reels/{reelsId}/user/{userId}")
    public ResponseEntity<Boolean> hasUserLikedReels(
            @PathVariable Long reelsId,
            @PathVariable Long userId) {

        boolean hasLiked = likeReelsService.hasUserLikedReel(reelsId, userId);
        return new ResponseEntity<>(hasLiked, HttpStatus.OK);
    }

    @GetMapping("/status/{statusId}/user/{userId}")
    public ResponseEntity<Boolean> hasUserLikedStatus(
            @PathVariable Long statusId,
            @PathVariable Long userId) {

        boolean hasLiked = likeStatusService.hasUserLikedStatus(statusId, userId);
        return new ResponseEntity<>(hasLiked, HttpStatus.OK);
    }

    @GetMapping("/comments/{commentId}/user/{userId}")
    public ResponseEntity<Boolean> hasUserLikedComment(
            @PathVariable String commentId,
            @PathVariable Long userId) {

        boolean hasLiked = commentLikeService.hasUserLikedComment(commentId, userId);
        return new ResponseEntity<>(hasLiked, HttpStatus.OK);
    }

    @GetMapping("/notification/{postOwnerId}")
    public PostNotificationDTO getNotificationsForUser(@PathVariable Long postOwnerId) {
        List<PostNotification> notifications = postNotificationRepository.findAll().stream()
                .filter(notification -> notification.getPostOwnerId().equals(postOwnerId))
                .sorted(Comparator.comparing(PostNotification::getCreatedAt).reversed())
                .collect(Collectors.toList());

        int count = notifications.size();
        return new PostNotificationDTO(notifications, count);
    }

    @GetMapping("/notification-status/{statusOwnerId}")
    public StatusNotificationDTO getNotificationForStatusUser(@PathVariable Long statusOwnerId){
        List<StatusNotification> statusNotifications = statusNotificationRepository.findAll().stream()
                .filter(statusNotification -> statusNotification.getStatusOwnerId().equals(statusOwnerId))
                .sorted(Comparator.comparing(StatusNotification::getCreatedAt).reversed())
                .collect(Collectors.toList());

        int count = statusNotifications.size();
        return new StatusNotificationDTO(statusNotifications,count);
    }

    @GetMapping("/notification-reels/{reelsOwnerId}")
    public ReelsNotificationDTO getNotificationForReelUser(@PathVariable Long reelsOwnerId){
        List<ReelsNotification> reelsNotifications = reelsNotificationRepository.findAll().stream()
                .filter(reelsNotification -> reelsNotification.getReelsOwnerId().equals(reelsOwnerId))
                .sorted(Comparator.comparing(ReelsNotification::getCreatedAt).reversed())
                .collect(Collectors.toList());

        int count = reelsNotifications.size();
        return new ReelsNotificationDTO(reelsNotifications,count);
    }

    @DeleteMapping("notification/{id}/{type}")
    public ResponseEntity<String> deleteNotification(@PathVariable String id,@PathVariable String type){
        switch (type) {
            case "POST-LIKE" -> postNotificationService.deleteNotificationForPost(id, type);
            case "REEL-LIKE" -> reelsNotificationService.deleteNotificationForReels(id, type);
            case "STATUS-LIKE" -> statusNotificationService.deleteNotificationForStatus(id, type);
            case "LIKE_COMMENT" -> commentLikeNotificationService.deleteNotificationForComment(id,type);
            default -> throw new IllegalArgumentException("No id found or type mismatch.");
        }
        return ResponseEntity.ok("Notification deleted");
    }

    @DeleteMapping("/notification/delete-all")
    public ResponseEntity<String> deleteAll(){
        List<PostNotification> notifications = postNotificationRepository.findAll();
        List<ReelsNotification> notifications1 = reelsNotificationRepository.findAll();
        List<StatusNotification> notifications2 = statusNotificationRepository.findAll();
        List<CommentLikeNotification> notifications3 = commentLikeNotificationRepository.findAll();

        if(!notifications.isEmpty() && !notifications1.isEmpty() && !notifications2.isEmpty() && !notifications3.isEmpty()){
            postNotificationRepository.deleteAll();
            reelsNotificationRepository.deleteAll();
            statusNotificationRepository.deleteAll();
            commentLikeNotificationRepository.deleteAll();
        }else {
            throw new IllegalArgumentException("No notifications to delete.");
        }
        return ResponseEntity.ok("Notifications deleted.");
    }

    @GetMapping("/all-post")
    public ResponseEntity<List<Like>> getAllPostLike(){
        try {
            List<Like> like = likeService.getAllLikeForPost();
            return ResponseEntity.status(HttpStatus.OK).body(like);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}

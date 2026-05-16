package com.Sadetechno.like_module.Service;

import com.Sadetechno.like_module.DTO.UserDTO;
import com.Sadetechno.like_module.FeignClient.StatusFeignClient;
import com.Sadetechno.like_module.FeignClient.UserFeignClient;
import com.Sadetechno.like_module.Repository.LikeStatusRepository;
import com.Sadetechno.like_module.model.LikeReels;
import com.Sadetechno.like_module.model.LikeStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeStatusService {

    @Autowired
    private LikeStatusRepository likeStatusRepository;

    @Autowired
    private StatusNotificationService statusNotificationService;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private StatusFeignClient statusFeignClient;

    private static final Logger logger = LoggerFactory.getLogger(LikeStatusService.class);

    public LikeStatus toggleLikeStatus(Long statusId, Long userId){

        Optional<LikeStatus> existingLikeStatus = likeStatusRepository.findByStatusIdAndUserId(statusId, userId);

        if(existingLikeStatus.isPresent()){
            likeStatusRepository.delete(existingLikeStatus.get());
            return null;
        }else{
            LikeStatus likeStatus = new LikeStatus();
            likeStatus.setStatusId(statusId);
            likeStatus.setUserId(userId);
            if(userId == statusFeignClient.getUserDetailsByStatusId(statusId).getUserId()){
                logger.warn("User id {} and status user id {} are same.So, user can't like his own status",userId,statusFeignClient.getUserDetailsByStatusId(statusId).getUserId());
                throw new IllegalArgumentException("User can't like his own status.");
            }else {
            likeStatusRepository.save(likeStatus);
            }

            UserDTO userDetail = userFeignClient.getUserById(userId);
            String profileImagePath = userDetail.getProfileImagePath();
            String userEmail = userDetail.getEmail();  // Get the email
            String userName =  userFeignClient.getUserById(userId).getName();

            Long statusOwnerId = statusFeignClient.getUserDetailsByStatusId(statusId).getUserId();
            logger.info("Got status owner id {} of status id {}",statusOwnerId,statusId);

            // Create a notification after saving the new request
            String notificationMessage = "liked your status.";

            if(userId == statusOwnerId){
                logger.warn("User id {} and status owner id {} are same.So, user can't like his own status",userId,statusOwnerId);
                throw new IllegalArgumentException("User can't like his own status.");
            }
            statusNotificationService.createStatusNotification(userId, notificationMessage, userEmail,"STATUS-LIKE" , likeStatus.getStatusId(), userName,profileImagePath,statusOwnerId);


            return likeStatusRepository.save(likeStatus);
        }
    }

    public List<LikeStatus> getLikesByStatusId(Long statusId) {
        return likeStatusRepository.findByStatusId(statusId);
    }

    public long getLikeCountByStatusId(Long statusId) {
        return likeStatusRepository.countByStatusId(statusId);
    }
    public List<Long> getUserIdsWhoLikedStatus(Long statusId) {
        return likeStatusRepository.findUserIdByStatusId(statusId);
    }

    public boolean hasUserLikedStatus(Long statusId, Long userId) {
        Optional<LikeStatus> existingLike = likeStatusRepository.findByStatusIdAndUserId(statusId, userId);
        return existingLike.isPresent();
    }
}

package com.Sadetechno.comment_module.Service;

import com.Sadetechno.comment_module.DTO.UserDTO;
import com.Sadetechno.comment_module.FeignClient.StatusFeignClient;
import com.Sadetechno.comment_module.FeignClient.UserFeignClient;
import com.Sadetechno.comment_module.Repository.CommentStatusRepository;
import com.Sadetechno.comment_module.model.CommentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentStatusService {

    @Autowired
    private CommentStatusRepository commentStatusRepository;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private StatusNotificationService statusNotificationService;

    @Autowired
    private StatusFeignClient statusFeignClient;

    private static final Logger logger = LoggerFactory.getLogger(CommentStatusService.class);

    public CommentStatus createCommentForStatus(Long statusId, Long userId, String textContent) {
            CommentStatus commentStatus = new CommentStatus();
            commentStatus.setUserId(userId);
            commentStatus.setStatusId(statusId);
            commentStatus.setTextContent(textContent);

        UserDTO userDetail = userFeignClient.getUserById(commentStatus.getUserId());
        String message = userDetail.getName() + " replied to your status.";
        logger.info("Comment sent successfully to status id {}",statusId);

        Long statusOwnerId = statusFeignClient.getUserDetailsByStatusId(commentStatus.getStatusId()).getUserId();

        if(userId == statusOwnerId){
        logger.warn("User id {} and Status owner id {}.If both are same, user can't comment to his own status ",userId,statusOwnerId);
            throw new IllegalArgumentException("User can't reply to his own status");
        }

            statusNotificationService.createNotificationForStatus(userId,message, userDetail.getEmail(),"STATUS-COMMENT", commentStatus.getStatusId(), userDetail.getName(), userDetail.getProfileImagePath(),statusOwnerId);
          return commentStatusRepository.save(commentStatus);
    }

    public List<CommentStatus> getCommentForStatus(Long userId) {
       return commentStatusRepository.findByUserId(userId);
    }

    public List<CommentStatus> getCommentStatusByStatusId(Long statusId){
        return commentStatusRepository.findByStatusId(statusId);
    }
}
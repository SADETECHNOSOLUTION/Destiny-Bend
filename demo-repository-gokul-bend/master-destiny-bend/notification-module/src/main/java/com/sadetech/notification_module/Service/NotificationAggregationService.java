package com.sadetech.notification_module.Service;

import com.sadetech.notification_module.FeignClient.CommentFeignClient;
import com.sadetech.notification_module.FeignClient.FollowFeignClient;
import com.sadetech.notification_module.FeignClient.FriendRequestFeignClient;
import com.sadetech.notification_module.FeignClient.LikeFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationAggregationService {

    @Autowired
    private LikeFeignClient likeFeignClient;

    @Autowired
    private CommentFeignClient commentFeignClient;

    @Autowired
    private FollowFeignClient followFeignClient;

    @Autowired
    private FriendRequestFeignClient friendRequestFeignClient;

    public void deleteNotification(String id, String type){
        switch (type){
            case "POST-LIKE":
            case "REEL-LIKE":
            case "STATUS-LIKE":
            case "LIKE_COMMENT":
                likeFeignClient.deleteNotification(id, type);
                break;

            case "POST-COMMENT":
            case "COMMENT-REPLY":
            case "REELS-COMMENT":
            case "STATUS-COMMENT":
                commentFeignClient.deleteNotification(id, type);
                break;

            case "FRIEND_ACCEPTED":
            case "FRIEND_DECLINED":
            case "FRIEND_PENDING":
                friendRequestFeignClient.deleteNotification(id, type);
                break;

            case "FOLLOW":
                followFeignClient.deleteNotification(id, type);
                break;

            default:
                throw new IllegalArgumentException("No id found or type mismatch.");
        }
    }
}

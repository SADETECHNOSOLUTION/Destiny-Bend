package com.sadetech.home_api_module.Service;

import com.sadetech.home_api_module.DTO.AggregateResponseDTO;
import com.sadetech.home_api_module.FeignClient.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AggregateService {

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private FollowerFeignClient followerFeignClient;

    @Autowired
    private FollowingFeignClient followingFeignClient;

    @Autowired
    private FriendRequestFeignClient friendRequestFeignClient;

    @Autowired
    private PostImageFeignClient postImageFeignClient;

    @Autowired
    private SendRequestFeignClient sendRequestFeignClient;

    @Autowired
    private PostVideoFeignClient postVideoFeignClient;

    public AggregateResponseDTO getAggregateData(Long userId) {
        try{
            AggregateResponseDTO aggregateResponse = new AggregateResponseDTO();

            // Fetching data from different microservices
            aggregateResponse.setUser(userFeignClient.getUserById(userId));
            aggregateResponse.setFollowers(followerFeignClient.getFollowers(userId));
            aggregateResponse.setFollowing(followingFeignClient.getFollowing(userId));
            aggregateResponse.setFriendListAndCount(friendRequestFeignClient.getFriendListAndCount(userId));
            aggregateResponse.setSentRequestDetails(sendRequestFeignClient.getSentRequestsDetails(userId));
            aggregateResponse.setImagePosts(postImageFeignClient.getImagePostsByUserId(userId));
            aggregateResponse.setVideoPosts(postVideoFeignClient.getVideoPostsByUserId(userId));

            return aggregateResponse;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

    }
}


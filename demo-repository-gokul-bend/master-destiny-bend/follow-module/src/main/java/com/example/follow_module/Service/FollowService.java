package com.example.follow_module.Service;

import com.example.follow_module.DTO.FollowResponseDTO;
import com.example.follow_module.DTO.UserDTO;
import com.example.follow_module.Feign.UserFeignClient;
import com.example.follow_module.Model.FollowModel;
import com.example.follow_module.Repository.FollowRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private NotificationService notificationService;

    public void followUser(Long followerId, Long followingId) {
        // Fetch both users by their IDs from the user-service
        UserDTO follower = getUserById(followerId, "Follower user not found.");
        UserDTO following = getUserById(followingId, "User to follow not found.");

        // Prevent a user from following themselves
        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("User cannot follow themselves.");
        }

        // Check if the follow relationship already exists
        if (followRepository.findByFollowerIdAndFollowingId(followerId, followingId).isPresent()) {
            throw new IllegalArgumentException("User is already following this user.");
        }

        // Save follow relationship
        FollowModel follow = new FollowModel();
        follow.setFollowerId(followerId);
        follow.setFollowingId(followingId);

        String notificationMessage = follower.getName() + " started following you.";
        String profileImagePath = follower.getProfileImagePath();

        notificationService.createNotification(followingId, notificationMessage, following.getEmail(),profileImagePath,"FOLLOW");

        followRepository.save(follow);
    }

    public void unfollowUser(Long followerId, Long followingId) {
        // Check if the follow relationship exists
        Optional<FollowModel> follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId);
        if (follow.isPresent()) {
            followRepository.delete(follow.get());
        } else {
            throw new IllegalArgumentException("Follow relationship does not exist.");
        }
    }

        public FollowResponseDTO getFollowers(Long id) {
            List<FollowModel> followers = followRepository.findAllByFollowingId(id);
            List<UserDTO> followerDetails = followers.stream()
                    .map(follow -> getUserById(follow.getFollowerId(), "Follower not found."))
                    .collect(Collectors.toList());

            String userName = getUserById(id, "User not found.").getName();
            String message;
            if(followerDetails.size()<= 1){
                 message = userName + " has " + followerDetails.size() + " follower";
            }else {
                 message = userName + " has " + followerDetails.size() + " followers";
            }
            return new FollowResponseDTO(message, followerDetails.size(), followerDetails);
        }

        public FollowResponseDTO getFollowing(Long id) {
            List<FollowModel> following = followRepository.findAllByFollowerId(id);
            List<UserDTO> followingDetails = following.stream()
                    .map(follow -> getUserById(follow.getFollowingId(), "Following user not found."))
                    .collect(Collectors.toList());

            String userName = getUserById(id, "User not found.").getName();

            String message;
            if(followingDetails.size()<= 1){
                message = userName + " is following " + followingDetails.size() + " person";
            }else {
                message = userName + " is following " + followingDetails.size() + " people";
            }

            return new FollowResponseDTO(message, followingDetails.size(), followingDetails);
        }

        // Helper method to fetch a user by ID and handle errors
        private UserDTO getUserById(Long id, String errorMessage) {
            try {
                return userFeignClient.getUserById(id);
            } catch (FeignException.NotFound e) {
                throw new IllegalArgumentException(errorMessage);
            } catch (FeignException e) {
                throw new RuntimeException("Error communicating with user-service: " + e.getMessage(), e);
            }
        }

}


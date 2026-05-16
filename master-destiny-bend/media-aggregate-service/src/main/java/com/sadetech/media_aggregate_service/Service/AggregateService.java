package com.sadetech.media_aggregate_service.Service;

import com.sadetech.media_aggregate_service.DTO.AggregateDTO;
import com.sadetech.media_aggregate_service.DTO.CommentStatusDTO;
import com.sadetech.media_aggregate_service.DTO.LikeStatusDTO;
import com.sadetech.media_aggregate_service.DTO.StatusDTO;
import com.sadetech.media_aggregate_service.FeignClient.*;

import com.sadetech.media_aggregate_service.PostDTO.*;
import com.sadetech.media_aggregate_service.ReelDTO.AllReelResponse;
import com.sadetech.media_aggregate_service.ReelDTO.CommentReelsResponse;
import com.sadetech.media_aggregate_service.ReelDTO.ReelAggregateResponse;
import com.sadetech.media_aggregate_service.ReelDTO.ReelResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AggregateService {

    @Autowired
    private LikesFeignClient likesFeignClient;

    @Autowired
    private CommentFeignClient commentFeignClient;

    @Autowired
    private StatusFeignClient statusFeignClient;

    @Autowired
    private PostFeignClient postFeignClient;

    @Autowired
    private PostCommentFeignClient postCommentFeignClient;

    @Autowired
    private PostLikeFeignClient postLikeFeignClient;

    @Autowired
    private ReelFeignClient reelFeignClient;

    @Autowired
    private ReelCommentFeignClient reelCommentFeignClient;

    @Autowired
    private ReelsLikeFeignClient reelsLikeFeignClient;

    @Autowired
    private AllPostFeignClient allPostFeignClient;

    @Autowired
    private AllReelFeignClient allReelFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private UserPostFeignClient userPostFeignClient;

    @Autowired
    private UserStatusFeignClient userStatusFeignClient;

    public AggregateDTO getStatusDetails(Long statusId){

        AggregateDTO aggregateDTO = new AggregateDTO();
        aggregateDTO.setStatusDTO(statusFeignClient.getUserDetailsByStatusId(statusId));
        aggregateDTO.setLikeStatusDTO(likesFeignClient.getLikesByStatusId(statusId));
        aggregateDTO.setCommentStatusDTO(commentFeignClient.getComments(statusId));
        return aggregateDTO;
    }

    public PostAggregateResponse getPostDetails(Long postId){
        PostAggregateResponse postAggregateResponse = new PostAggregateResponse();
        postAggregateResponse.setPostResponseDTO(postFeignClient.getPostWithUserDetails(postId));
        postAggregateResponse.setCommentResponses(postCommentFeignClient.getCommentsByPostId(postId));
        postAggregateResponse.setLikeDTO(postLikeFeignClient.getLikesByPostId(postId));
        return postAggregateResponse;
    }

    public List<UserPostAggregateResponse> getPostDetailsByUserId(Long userId) {
        List<PostResponseDTO> allPost = userPostFeignClient.getPostsByUserId(userId);

        // If no posts, return empty list
        if (allPost == null || allPost.isEmpty()) {
            return Collections.emptyList();
        }

        return allPost.stream()
                .map(postResponseDTO -> {
                    // Fetch likes for the post, or an empty map if not found
                    Map<String, Object> likes = Optional.ofNullable(
                            postLikeFeignClient.getLikesByPostId(postResponseDTO.getPostId())
                    ).orElse(Collections.emptyMap());

                    // Fetch comments for the post, or an empty list if not found
                    List<CommentResponse> comments = Optional.ofNullable(
                            postCommentFeignClient.getCommentsByPostId(postResponseDTO.getPostId())
                    ).orElse(Collections.emptyList());

                    // Return the aggregated response for this post
                    return new UserPostAggregateResponse(postResponseDTO, likes, comments);
                })
                .collect(Collectors.toList());
    }

    public List<AggregateDTO> getStatusDetailsByUserId(Long userId) {
        List<StatusDTO> allStatuses = userStatusFeignClient.getStatusesByUserId(userId);

        if (allStatuses == null || allStatuses.isEmpty()) {
            return Collections.emptyList();
        }

        return allStatuses.stream()
                .map(statusDTO -> {
                    List<LikeStatusDTO> likeStatusDTOS = Collections.emptyList();
                    List<CommentStatusDTO> commentStatusDTOS = Collections.emptyList();
                    int likesCount = 0;
                    int commentCount = 0;

                    try {
                        likeStatusDTOS = Optional.ofNullable(likesFeignClient.getLikesByStatusId(statusDTO.getId()))
                                .orElse(Collections.emptyList());
                        likesCount = likeStatusDTOS.size(); // Store count
                    } catch (Exception e) {
                        System.err.println("Error fetching likes for statusId: " + statusDTO.getId() + " - " + e.getMessage());
                    }

                    try {
                        commentStatusDTOS = Optional.ofNullable(commentFeignClient.getComments(statusDTO.getId()))
                                .orElse(Collections.emptyList());
                        commentCount = commentStatusDTOS.size(); // Store count
                    } catch (Exception e) {
                        System.err.println("Error fetching comments for statusId: " + statusDTO.getId() + " - " + e.getMessage());
                    }

                    return new AggregateDTO(commentStatusDTOS, likeStatusDTOS, statusDTO, likesCount, commentCount);
                })
                .collect(Collectors.toList());
    }


    public ReelAggregateResponse getReelsDetails(Long reelsId){
        ReelAggregateResponse reelAggregateResponse = new ReelAggregateResponse();
        reelAggregateResponse.setReelResponse(reelFeignClient.getUserDetailsByReelsId(reelsId));
        reelAggregateResponse.setCommentReelsResponse(reelCommentFeignClient.getCommentForReels(reelsId));
        reelAggregateResponse.setReelDTO(reelsLikeFeignClient.getLikesByReelsId(reelsId));
        return reelAggregateResponse;
    }

    public List<AllPostResponse> getAllPostAggregates(int page, int size) {
        // Fetch all posts and handle null/empty response
        List<PostResponseDTO> allPosts = allPostFeignClient.getAllPosts(page, size);
        if (allPosts == null || allPosts.isEmpty()) {
            return Collections.emptyList(); // No posts found
        }

        // Fix size to 5 regardless of input
        size = 5;

        // Calculate start and end indices for pagination
        int start = page * size;
        int end = Math.min(start + size, allPosts.size());

        // Handle case where page number is out of bounds
        if (start >= allPosts.size()) {
            return Collections.emptyList();
        }

        // Paginate posts
        List<PostResponseDTO> paginatedPosts = allPosts.subList(start, end);

        // Map posts to the response
        return paginatedPosts.stream()
                .map(post -> {
                    // Fetch likes and comments with safe default values
                    Map<String, Object> likes = Optional.ofNullable(
                            postLikeFeignClient.getLikesByPostId(post.getPostId())
                    ).orElse(Collections.emptyMap());

                    List<CommentResponse> comments = Optional.ofNullable(
                            postCommentFeignClient.getCommentsByPostId(post.getPostId())
                    ).orElse(Collections.emptyList());

                    // Construct and return the response
                    return new AllPostResponse(post, likes, comments);
                })
                .collect(Collectors.toList());
    }


    public List<AllReelResponse> getAllReel(int page, int size){
        List<ReelResponse> getAllReels = allReelFeignClient.getAllReels(page, size);

        size = 5;

        int start = page * size;
        int end = Math.min(start + size, getAllReels.size());

        if(start >= getAllReels.size()){
            return Collections.emptyList();
        }

        List<ReelResponse> paginatedReel = getAllReels.subList(start,end);
        return paginatedReel.stream()
                .map(reel -> {
                    Map<String, Object> likes = reelsLikeFeignClient.getLikesByReelsId(reel.getId());
                    List<CommentReelsResponse> commentResponses = reelCommentFeignClient.getCommentForReels(reel.getId());
                    return new AllReelResponse(reel,likes,commentResponses);
                })
                .collect(Collectors.toList());
    }


}

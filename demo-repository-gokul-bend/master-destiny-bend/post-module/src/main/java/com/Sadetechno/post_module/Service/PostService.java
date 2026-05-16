package com.Sadetechno.post_module.Service;
import com.Sadetechno.post_module.DTO.ResponseDTO;
import com.Sadetechno.post_module.DTO.UserDTO;
import com.Sadetechno.post_module.FeignClient.FriendRequestFeignClient;
import com.Sadetechno.post_module.FeignClient.UserFeignClient;
import com.Sadetechno.post_module.Repository.PostRepository;
import com.Sadetechno.post_module.model.Post;
import com.Sadetechno.post_module.model.PostType;
import com.Sadetechno.post_module.model.PostVisibility;
import com.Sadetechno.post_module.model.PrivacySetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private FriendRequestFeignClient friendRequestFeignClient;

    @Autowired
    private FileUploadService fileUploadService; // Injecting FileUploadService

    public Post createPost(Long userId, String postType, MultipartFile imageFile, MultipartFile videoFile, String description, String privacySetting, String postVisibility,String parentName, String parentProfileImagePath, String parentDescription, String parentPostContent, String parentPostCreatedAt) throws IOException {
        Post post = new Post();
        PostType type = PostType.valueOf(postType);
        post.setPostType(type);
        post.setUserId(userId);
        post.setParentName(parentName);
        post.setParentProfileImagePath(parentProfileImagePath);
        post.setParentDescription(parentDescription);
        post.setParentPostContent(parentPostContent);
        post.setParentPostCreatedAt(parentPostCreatedAt);

        PrivacySetting privacy = PrivacySetting.valueOf(privacySetting.toUpperCase());
        post.setPrivacySetting(privacy);  // Set privacy setting

        PostVisibility postVisibility1 = PostVisibility.valueOf(postVisibility.toUpperCase());
        post.setPostVisibility(postVisibility1);

        switch (type) {
            case TEXT:
                if(description != null && !description.isEmpty()){
                    post.setDescription(description);
                }else {
                    throw new IllegalArgumentException("Text required to post.");
                }
                break;

            case IMAGE:
                if (imageFile != null && !imageFile.isEmpty()) {
                    String imageUrl = fileUploadService.uploadFile(imageFile);
                    post.setImageUrl(imageUrl);
                } else {
                    throw new IllegalArgumentException("Image file is required for IMAGE post type.");
                }
                break;
            case VIDEO:
                if (videoFile != null && !videoFile.isEmpty()) {
                    String videoUrl = fileUploadService.uploadFile(videoFile);
                    post.setVideoUrl(videoUrl);
                } else {
                    throw new IllegalArgumentException("Video file is required for VIDEO post type.");
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid post type");
        }

        return postRepository.save(post);
    }

    public ResponseDTO getPost(Long postId) {
        // Fetch the post by postId
        Post post = postRepository.findById(postId).orElse(null);

        if (post != null) {
            // Fetch the userId from the post
            String userId = String.valueOf(post.getUserId());

            // Fetch the profileImagePath using Feign client
            String profileImagePath = userFeignClient.getUserById(post.getUserId()).getProfileImagePath();

            String name = userFeignClient.getUserById(post.getUserId()).getName();

            // Return ResponseDTO with userId, profileImagePath, and post details
            return new ResponseDTO(
                    userId,
                    profileImagePath,
                    name,
                    post.getPostId(),        // Post ID
                    post.getDescription(),   // Post description
                    post.getImageUrl(),      // Image URL
                    post.getVideoUrl(),      // Video URL
                    post.getPrivacySetting().name(), // Privacy setting as a string
                    post.getCreatedAt(),      // Creation timestamp
                    post.getPostType(),
                    post.getPostVisibility().name()
            );
        } else {
            throw new IllegalArgumentException("Post ID not found.");
        }
    }


    public List<ResponseDTO> getAllPost() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(post -> {
            String userId = String.valueOf(post.getUserId());
            String profileImagePath = userFeignClient.getUserById(post.getUserId()).getProfileImagePath();

            String name = userFeignClient.getUserById(post.getUserId()).getName();


            // Return ResponseDTO with post details
            return new ResponseDTO(
                    userId,
                    profileImagePath,
                    name,
                    post.getPostId(),
                    post.getDescription(),
                    post.getImageUrl(),
                    post.getVideoUrl(),
                    post.getPrivacySetting().name(),
                    post.getCreatedAt(),
                    post.getPostType(),
                    post.getPostVisibility().name()
            );
        }).collect(Collectors.toList());
    }

    public List<ResponseDTO> getPostsByUserId(Long userId) {
        List<Post> posts = postRepository.findByUserIdOrderByCreatedAtDesc(userId);
        String profileImagePath = userFeignClient.getUserById(userId).getProfileImagePath();

        String name = userFeignClient.getUserById(userId).getName();


        return posts.stream().map(post -> new ResponseDTO(
                String.valueOf(userId),
                profileImagePath,
                name,
                post.getPostId(),
                post.getDescription(),
                post.getImageUrl(),
                post.getVideoUrl(),
                post.getPrivacySetting().name(),
                post.getCreatedAt(),
                post.getPostType(),
                post.getPostVisibility().name()
        )).collect(Collectors.toList());
    }

    public List<ResponseDTO> getImagePostsByUserId(Long userId) {
        List<Post> posts = postRepository.findByUserIdAndPostTypeOrderByCreatedAtDesc(userId, PostType.IMAGE);
        String profileImagePath = userFeignClient.getUserById(userId).getProfileImagePath();

        String name = userFeignClient.getUserById(userId).getName();


        return posts.stream().map(post -> new ResponseDTO(
                String.valueOf(userId),
                profileImagePath,
                name,
                post.getPostId(),
                post.getDescription(),
                post.getImageUrl(),
                post.getVideoUrl(),
                post.getPrivacySetting().name(),
                post.getCreatedAt(),
                post.getPostType(),
                post.getPostVisibility().name()
        )).collect(Collectors.toList());
    }

    public List<ResponseDTO> getVideoPostsByUserId(Long userId) {
        List<Post> posts = postRepository.findByUserIdAndPostTypeOrderByCreatedAtDesc(userId, PostType.VIDEO);
        String profileImagePath = userFeignClient.getUserById(userId).getProfileImagePath();

        String name = userFeignClient.getUserById(userId).getName();


        return posts.stream().map(post -> new ResponseDTO(
                String.valueOf(userId),
                profileImagePath,
                name,
                post.getPostId(),
                post.getDescription(),
                post.getImageUrl(),
                post.getVideoUrl(),
                post.getPrivacySetting().name(),
                post.getCreatedAt(),
                post.getPostType(),
                post.getPostVisibility().name()
        )).collect(Collectors.toList());
    }

    public void deletePost(Long postId) throws IOException {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null) {
            // Delete associated files if they exist
            if (post.getImageUrl() != null) {
                fileUploadService.deleteFile(post.getImageUrl());
            }
            if (post.getVideoUrl() != null) {
                fileUploadService.deleteFile(post.getVideoUrl());
            }
            postRepository.delete(post);
        }
    }

    public Post changeVisibilityType(Long userId,PostVisibility postVisibility){
        Optional<Post> postUserId = postRepository.findByUserId(userId);
        if(postUserId.isPresent()){
          Post existingPostVisibility =   postUserId.get();
            existingPostVisibility.setPostVisibility(postVisibility);
           return postRepository.save(existingPostVisibility);
        }else {
            throw new IllegalArgumentException("No user id found");
        }
    }

    public List<ResponseDTO> getPostByUserIdAndPostVisibility(Long userId, PostVisibility postVisibility){
        List<Post> posts = postRepository.findByUserIdAndPostVisibility(userId,postVisibility);
        String profileImagePath = userFeignClient.getUserById(userId).getProfileImagePath();

        String name = userFeignClient.getUserById(userId).getName();


        return posts.stream().map(post -> new ResponseDTO(
                String.valueOf(userId),
                profileImagePath,
                name,
                post.getPostId(),
                post.getDescription(),
                post.getImageUrl(),
                post.getVideoUrl(),
                post.getPrivacySetting().name(),
                post.getCreatedAt(),
                post.getPostType(),
                post.getPostVisibility().name()
        )).collect(Collectors.toList());
    }

    public ResponseDTO getPostByIdAndPrivacySetting(Long postId, Long userId){
       Post post = postRepository.findById(postId).orElse(null);
       if(post == null){
           throw new IllegalArgumentException("Post id not found");
       }

       PrivacySetting privacySetting = post.getPrivacySetting();
       switch (privacySetting){
           case PUBLIC :
               return createResponseDTO(post);
           case PRIVATE :
               if (post.getUserId().equals(userId)){
                   return createResponseDTO(post);
               }else {
                   throw new IllegalArgumentException("You do not have permission to access the post, because it is private.");
               }
           case FRIENDS:
               if(post.getUserId().equals(userId)){
                   return createResponseDTO(post);
               }else {
                   Map<String, Object> friendListResponse = friendRequestFeignClient.getFriendListAndCount(post.getUserId());
                   List<?> friends = (List<?>) friendListResponse.get("friends");

                   // Convert each LinkedHashMap in the friends list to UserDTO
                   List<Long> friendIds = friends.stream()
                           .map(friend -> {
                               Map<String, Object> friendMap = (Map<String, Object>) friend;  // Cast to Map
                               return Long.parseLong(friendMap.get("id").toString());  // Extract userId
                           })
                           .collect(Collectors.toList());

                   if (friendIds.contains(userId)) {
                       return createResponseDTO(post); // Friend can see the post
                   } else {
                       throw new IllegalArgumentException("You do not have permission to view this post, because you are not friends with the owner of the post.");
                   }
               }
           default:
               throw new IllegalArgumentException("Invalid privacy setting.");
       }

    }

    private ResponseDTO createResponseDTO(Post post) {
        String userId = String.valueOf(post.getUserId());
        String profileImagePath = userFeignClient.getUserById(post.getUserId()).getProfileImagePath();
        String name = userFeignClient.getUserById(post.getUserId()).getName();

        return new ResponseDTO(
                userId,
                profileImagePath,
                name,
                post.getPostId(),
                post.getDescription(),
                post.getImageUrl(),
                post.getVideoUrl(),
                post.getPrivacySetting().name(),
                post.getCreatedAt(),
                post.getPostType(),
                post.getPostVisibility().name()
        );
    }

    public Post updatePrivacy(Long postId, PrivacySetting privacy){
        Optional<Post> post = postRepository.findById(postId);
        if(post.isPresent()){
            Post post1 = post.get();
            post1.setPrivacySetting(privacy);
            return postRepository.save(post1);
        }else {
            throw new IllegalArgumentException("Post Id not found");
        }
    }


}
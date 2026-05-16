package com.sadetech.reels_module.Service;

import com.sadetech.reels_module.Dto.ReelResponseDTO;
import com.sadetech.reels_module.Dto.UserDTO;
import com.sadetech.reels_module.FeignClient.FriendRequestFeignClient;
import com.sadetech.reels_module.FeignClient.UserFeignClient;
import com.sadetech.reels_module.Model.Privacy;
import com.sadetech.reels_module.Model.Reel;
import com.sadetech.reels_module.Repository.ReelRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReelService {

    @Autowired
    private ReelRepository reelRepository;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private FriendRequestFeignClient friendRequestFeignClient;

    private static final int MAX_VIDEO_LENGTH = 30;

    public Reel saveReels(MultipartFile file, String caption, int duration, Long userId, Privacy privacy) throws IOException {
        String filePath = fileUploadService.uploadFile(file);

        String contentType = file.getContentType();
        if (contentType != null && contentType.startsWith("video")) {
            if (duration > MAX_VIDEO_LENGTH) {
                throw new IllegalArgumentException("Video length cannot exceed 30 seconds");
            }
        } else {
            throw new IllegalArgumentException("Only video content is allowed");
        }

        // Fetch user details using Feign client
        UserDTO userDTO = getUserById(userId);
        String profileImagePath = userDTO.getProfileImagePath();

        Reel reel = new Reel();
        reel.setContent(filePath);
        reel.setCaption(caption);
        reel.setCreatedAt(LocalDateTime.now());
        reel.setDuration(duration);
        reel.setUserId(userId);
        reel.setPrivacy(privacy);
        return reelRepository.save(reel);
    }

    public List<ReelResponseDTO> getReelsByUserId(Long userId) {
        // Fetch all reels by userId
        List<Reel> reels = reelRepository.findByUserId(userId);

        // Fetch user profile image path using Feign client
        UserDTO userDTO = userFeignClient.getUserById(userId);  // Call user service
        String profileImagePath = userDTO.getProfileImagePath();  // Extract profile image path

        // Map reels to ReelResponseDTO, including profileImagePath
        List<ReelResponseDTO> responseDTOs = reels.stream()
                .map(reel -> new ReelResponseDTO(
                        reel.getId(),
                        userDTO.getName(),
                        reel.getContent(),
                        reel.getCaption(),
                        reel.getDuration(),
                        reel.getCreatedAt(),
                        reel.getUserId(),
                        profileImagePath,
                        reel.getPrivacy()// Add the profile image path to the response
                ))
                .collect(Collectors.toList());

        return responseDTOs;
    }

    public List<ReelResponseDTO> getAllReels() {
        // Fetch all reels
        List<Reel> reels = reelRepository.findAll();

        // Map each reel to ReelResponseDTO, including profileImagePath
        List<ReelResponseDTO> responseDTOs = reels.stream()
                .map(reel -> {
                    // Fetch the user info using Feign client for each reel
                    UserDTO userDTO = userFeignClient.getUserById(reel.getUserId());
                    String profileImagePath = userDTO.getProfileImagePath();  // Extract profile image path

                    // Map Reel to ReelResponseDTO, including profile image path
                    return new ReelResponseDTO(
                            reel.getId(),
                            userDTO.getName(),
                            reel.getContent(),
                            reel.getCaption(),
                            reel.getDuration(),
                            reel.getCreatedAt(),
                            reel.getUserId(),
                            profileImagePath,  // Add the profile image path to the response
                            reel.getPrivacy()
                    );
                })
                .collect(Collectors.toList());

        return responseDTOs;
    }

    public String deleteReelByUserId(Long userId, Long id) {
        if (!reelRepository.existsByUserIdAndId(userId, id)) {
            throw new EntityNotFoundException("Reel with ID " + id + " for user ID " + userId + " not found.");
        }
        reelRepository.deleteReelByUserIdAndId(userId, id);
        return "Reel with ID " + id + " for user ID " + userId + " deleted successfully.";
    }

    private UserDTO getUserById(Long userId){
        return userFeignClient.getUserById(userId);
    }

    public ReelResponseDTO getUserDetailsByReelsId(Long id) {
        Optional<Reel> reel = reelRepository.findById(id);
        if (reel.isEmpty()){
            throw new IllegalArgumentException("No reel present for the id");
        }
        Reel getReel = reel.get();
        String profileImagePath = userFeignClient.getUserById(getReel.getUserId()).getProfileImagePath();
        String name = userFeignClient.getUserById(getReel.getUserId()).getName();
        return new ReelResponseDTO(
                getReel.getId(),
                name,
                getReel.getContent(),
                getReel.getCaption(),
                getReel.getDuration(),
                getReel.getCreatedAt(),
                getReel.getUserId(),
                profileImagePath,
                getReel.getPrivacy()
        );
    }


    public ReelResponseDTO getReelsByPrivacy(Long id, Long userId){
        Optional<Reel> reel = reelRepository.findById(id);
        if (reel.isEmpty()){
            throw new IllegalArgumentException("No reel id found");
        }
        Privacy privacy = reel.get().getPrivacy();
        switch (privacy){
            case PUBLIC:
                return createResponse(reel.get());

            case PRIVATE:
                if(reel.get().getUserId().equals(userId)){
                    return createResponse(reel.get());
                }else {
                    throw new IllegalArgumentException("You don't have access to view the reel. Because, it is in private.");
                }

            case FRIENDS:
                if(reel.get().getUserId().equals(userId)){
                    return createResponse(reel.get());
                }else {
                    Map<String, Object> friendListResponse = friendRequestFeignClient.getFriendListAndCount(reel.get().getUserId());
                    List<?> friends = (List<?>) friendListResponse.get("friends");

                    // Convert LinkedHashMap in friends list to UserDTO and get the user IDs
                    List<Long> friendIds = friends.stream()
                            .map(friend -> {
                                Map<String, Object> friendMap = (Map<String, Object>) friend;  // Cast to Map
                                return Long.parseLong(friendMap.get("id").toString());  // Extract userId
                            })
                            .collect(Collectors.toList());

                    if (friendIds.contains(userId)) {
                        return createResponse(reel.get()); // Friend can view the status
                    } else {
                        throw new IllegalArgumentException("You do not have permission to view this status, because you are not friends with the owner of the status.");
                    }
                }

            default:
                throw new IllegalArgumentException("Invalid privacy setting.");

        }

    }

    private ReelResponseDTO createResponse(Reel reel){

        UserDTO userDTO = userFeignClient.getUserById(reel.getUserId());
        String profileImagePath = userDTO.getProfileImagePath();

        return new ReelResponseDTO(
                reel.getId(),
                userDTO.getName(),
                reel.getContent(),
                reel.getCaption(),
                reel.getDuration(),
                reel.getCreatedAt(),
                reel.getUserId(),
                profileImagePath,
                reel.getPrivacy()
        );
    }

    public Reel updateReelPrivacy(Long id, Privacy privacy){
        Optional<Reel> getReel = reelRepository.findById(id);

        if(getReel.isPresent()){
            Reel reel = getReel.get();
            reel.setPrivacy(privacy);
            return reelRepository.save(reel);
        }else {
            throw new IllegalArgumentException("Reel id not found");
        }
    }
}
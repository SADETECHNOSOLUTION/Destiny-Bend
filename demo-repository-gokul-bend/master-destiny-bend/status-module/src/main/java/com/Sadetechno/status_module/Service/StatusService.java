package com.Sadetechno.status_module.Service;

import com.Sadetechno.status_module.Dto.StatusResponseDTO;
import com.Sadetechno.status_module.Dto.UserDTO;
import com.Sadetechno.status_module.FeignClient.FriendRequestFeignClient;
import com.Sadetechno.status_module.FeignClient.UserFeignClient;
import com.Sadetechno.status_module.Repository.StatusRepository;
import com.Sadetechno.status_module.model.Privacy;
import com.Sadetechno.status_module.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class StatusService {

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private FriendRequestFeignClient friendRequestFeignClient;

    private static final Logger logger = LoggerFactory.getLogger(StatusService.class);

    private static final int MAX_VIDEO_LENGTH = 50; // 50 seconds
    private static final int MAX_IMAGE_TEXT_LENGTH = 35; // 35 seconds

    public Status saveStatus(MultipartFile file, String type, int duration, Privacy privacy, Long userId) throws IOException {
        String filePath = fileUploadService.uploadFile(file);

        Status status = new Status();
        status.setContent(filePath);
        status.setType(type);
        status.setCreatedAt(LocalDateTime.now());
        status.setDuration(duration);
        status.setPrivacy(privacy);
        status.setUserId(userId);


        switch (type.toLowerCase()) {
            case "video":
                if (duration > MAX_VIDEO_LENGTH) {
                    throw new IllegalArgumentException("Video length cannot exceed 50 seconds");
                }
                break;
            case "image":
            case "text":
                if (duration > MAX_IMAGE_TEXT_LENGTH) {
                    throw new IllegalArgumentException("Image or text length cannot exceed 35 seconds");
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid content type");
        }

        return statusRepository.save(status);
    }

    public List<StatusResponseDTO> getStatusesByUserId(Long userId) {
        List<Status> statuses = statusRepository.findByUserId(userId);

        List<StatusResponseDTO> response = (List<StatusResponseDTO>) statuses.stream()
                .map(status ->{
                    UserDTO userDTO = userFeignClient.getUserById(status.getUserId());
                    String profileImagePath = userDTO.getProfileImagePath();

                    return new StatusResponseDTO(
                            status.getId(),
                            status.getContent(),
                            status.getType(),
                            status.getCreatedAt(),
                            status.getDuration(),
                            status.getPrivacy(),
                            status.getUserId(),
                            profileImagePath
                    );
                })
                .collect(Collectors.toList());

        return response;
    }

    public List<StatusResponseDTO>getAllStatus(){
        List<Status> statuses = statusRepository.findAll();

        List<StatusResponseDTO> responseDTO = statuses.stream()
                .map(status -> {
                    UserDTO userDTO = userFeignClient.getUserById(status.getUserId());
                    String profileImagePath = userDTO.getProfileImagePath();

                    return new StatusResponseDTO(
                            status.getId(),
                            status.getContent(),
                            status.getType(),
                            status.getCreatedAt(),
                            status.getDuration(),
                            status.getPrivacy(),
                            status.getUserId(),
                            profileImagePath
                    );
                })
                .collect(Collectors.toList());

        return responseDTO;
    }

    public void deleteStatus(Long userId, Long id){
        UserDTO user = userFeignClient.getUserById(userId);

        if(user == null){
            throw new IllegalArgumentException("No user found.");
        }

        Status status = statusRepository.findByUserIdAndId(userId,id)
                        .orElseThrow(()-> new IllegalArgumentException("Status not found for the id."));

        statusRepository.delete(status);


    }

    public Optional<Status> getUserDetailsByStatusId(Long id) {
        logger.info("Got the user");
        logger.error("Status id found {}",id);
        return statusRepository.findById(id);
    }

    public StatusResponseDTO getStatusByPrivacy(Long id, Long userId) {
        Optional<Status> status = statusRepository.findById(id);
        if (status.isEmpty()) {
            throw new IllegalArgumentException("Status not found");
        }

        Privacy privacy = status.get().getPrivacy();
        switch (privacy) {
            case PUBLIC:
                return createResponse(status.get());

            case FRIENDS:
                if (status.get().getUserId().equals(userId)) {
                    return createResponse(status.get());
                } else {
                    Map<String, Object> friendListResponse = friendRequestFeignClient.getFriendListAndCount(status.get().getUserId());
                    List<?> friends = (List<?>) friendListResponse.get("friends");

                    // Convert LinkedHashMap in friends list to UserDTO and get the user IDs
                    List<Long> friendIds = friends.stream()
                            .map(friend -> {
                                Map<String, Object> friendMap = (Map<String, Object>) friend;  // Cast to Map
                                return Long.parseLong(friendMap.get("id").toString());  // Extract userId
                            })
                            .collect(Collectors.toList());

                    if (friendIds.contains(userId)) {
                        return createResponse(status.get()); // Friend can view the status
                    } else {
                        throw new IllegalArgumentException("You do not have permission to view this status, because you are not friends with the owner of the status.");
                    }
                }

            default:
                throw new IllegalArgumentException("Invalid privacy setting.");
        }
    }


    private StatusResponseDTO createResponse(Status status){
        String profileImagePath = userFeignClient.getUserById(status.getUserId()).getProfileImagePath();

        return new StatusResponseDTO(
                status.getId(),
                status.getContent(),
                status.getType(),
                status.getCreatedAt(),
                status.getDuration(),
                status.getPrivacy(),
                status.getUserId(),
                profileImagePath
        );
    }

    public Status updateStatusPrivacy(Long id, Privacy privacy){
        Optional<Status> getStatus = statusRepository.findById(id);

        if(getStatus.isPresent()){
            Status status = getStatus.get();
            status.setPrivacy(privacy);
            return statusRepository.save(status);
        }else {
            throw new IllegalArgumentException("Status Id not found");
        }
    }
}

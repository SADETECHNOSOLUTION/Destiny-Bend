package com.sadetech.home_api_module.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AggregateResponseDTO {
        private UserDTO user;
        private FollowResponseDTO followers;
        private FollowResponseDTO following;
        private Map<String, Object> friendListAndCount;
        private Map<String,Object> sentRequestDetails;
        private List<ResponseDTO> imagePosts;
        private List<ResponseDTO> videoPosts;

        // Getters and Setters
}


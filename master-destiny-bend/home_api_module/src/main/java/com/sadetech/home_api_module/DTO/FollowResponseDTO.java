package com.sadetech.home_api_module.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowResponseDTO {
    private String message;  // e.g., "Gokul has X followers" or "Gokul is following X people"
    private int count;       // Number of followers/following
    private List<UserDTO> users;       // Number of followers/following
}
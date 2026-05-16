package com.sadetech.friend_request_module.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String name;
    private String profileImagePath;
    private String bannerImagePath;
    private LocalDateTime requestTime;
    private String email;
    private String visibility;
    // Constructors, Getters, Setters
}

package com.sadetech.friend_request_module.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestResponseDto {

        private String message;
        private int count;
        private List<UserDTO> users;
        private LocalDateTime requestTime;
        private String email;
    }


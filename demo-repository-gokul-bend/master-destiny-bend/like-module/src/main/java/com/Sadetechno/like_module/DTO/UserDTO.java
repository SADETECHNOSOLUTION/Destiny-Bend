package com.Sadetechno.like_module.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long userId;
    private Long postId;
    private String name;
    private String email;
    private String profileImagePath;
}

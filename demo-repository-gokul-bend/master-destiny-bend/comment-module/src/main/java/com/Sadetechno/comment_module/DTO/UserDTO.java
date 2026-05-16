package com.Sadetechno.comment_module.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String name;
    private String profileImagePath;
    private Long userId;
    private Long postId;
    private Long repliedToUserId;
    private Long parentId;
    private String email;
}

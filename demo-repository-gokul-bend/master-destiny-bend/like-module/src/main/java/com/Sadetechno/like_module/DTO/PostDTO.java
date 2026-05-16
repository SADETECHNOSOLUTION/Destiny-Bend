package com.Sadetechno.like_module.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

    private String profileImagePath;
    private String name;
    private Long postId;
    private Long userId;

}

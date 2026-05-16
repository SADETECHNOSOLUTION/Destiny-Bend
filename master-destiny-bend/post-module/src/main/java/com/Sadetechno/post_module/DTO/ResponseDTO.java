package com.Sadetechno.post_module.DTO;

import com.Sadetechno.post_module.model.Post;
import com.Sadetechno.post_module.model.PostType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {

    private String userId;
    private String profileImagePath;
    private String name;
    private Long postId;
    private String description;
    private String imageUrl;
    private String videoUrl;
    private String privacySetting;
    private Date createdAt;
    private PostType postType;
    private String postVisibility;
}

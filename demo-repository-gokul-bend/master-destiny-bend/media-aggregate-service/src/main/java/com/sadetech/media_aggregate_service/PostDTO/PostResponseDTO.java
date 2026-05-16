package com.sadetech.media_aggregate_service.PostDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {
    private Long userId;
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

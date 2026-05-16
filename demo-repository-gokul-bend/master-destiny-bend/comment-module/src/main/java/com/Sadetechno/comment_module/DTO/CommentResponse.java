package com.Sadetechno.comment_module.DTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class CommentResponse {
    private String id;
    private Long postId;
    private Long userId;
    private String name;
    private Long repliedToUserId;
    private String textContent;
    private String imagePath;
    private LocalDateTime createdAt;
    private String profileImagePath;
    private String parentIdName;
    private Set<CommentResponse> replies;
    private Set<CommentLike> likes; // Add this field
    private int likeCount;
}

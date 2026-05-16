package com.sadetech.media_aggregate_service.ReelDTO;

import com.sadetech.media_aggregate_service.PostDTO.CommentLike;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReelsResponse {
    private String id;
    private Long reelsId;
    private Long userId;
    private String name;
    private Long repliedToUserId;
    private String textContent;
    private String imagePath;
    private LocalDateTime createdAt;
    private String profileImagePath;
    private String parentIdName;
    private Set<CommentReelsResponse> replies;
    private Set<CommentLike> likes; // Add this field
    private int likeCount;
}

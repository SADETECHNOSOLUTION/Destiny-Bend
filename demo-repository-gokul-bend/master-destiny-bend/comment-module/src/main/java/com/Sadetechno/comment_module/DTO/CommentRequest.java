package com.Sadetechno.comment_module.DTO;

import lombok.Data;

@Data
public class CommentRequest {

    private Long postId;
    private String parentId;
    private Long userId;
    private Long repliedToUserId;
    private String textContent;
    private String profileImagePath;
}
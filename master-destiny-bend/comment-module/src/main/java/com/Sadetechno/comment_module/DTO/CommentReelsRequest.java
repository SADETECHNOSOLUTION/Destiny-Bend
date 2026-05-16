package com.Sadetechno.comment_module.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReelsRequest {

    private Long reelsId;
    private String parentId;
    private Long userId;
    private Long repliedToUserId;
    private String textContent;
    private String profileImagePath;
}

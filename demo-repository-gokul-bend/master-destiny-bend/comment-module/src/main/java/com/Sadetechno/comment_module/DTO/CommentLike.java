package com.Sadetechno.comment_module.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentLike {

    private String id;
    private String commentId;
    private Long userId;
    private CommentLikeType commentLikeType;

    @CreatedDate
    private LocalDateTime createdAt;
}

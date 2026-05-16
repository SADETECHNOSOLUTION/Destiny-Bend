package com.Sadetechno.like_module.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "comment_like")
public class CommentLike {

    @Id
    private String id;
    private String commentId;
    private Long userId;
    private CommentLikeType commentLikeType;

    @CreatedDate
    private LocalDateTime createdAt;
}

package com.Sadetechno.like_module.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "post_notification")
public class PostNotification {

    @Id
    private String id;
    private Long userId;
    private String message;
    private String email;
    private Long postId;
    private String name;
    private String profileImagePath;
    private String type;
    private Long postOwnerId;

    @CreatedDate
    private LocalDateTime createdAt;

}

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
@Document(collection = "reels_notification")
public class ReelsNotification {

    @Id
    private String id;
    private Long userId;
    private String message;
    private String email;
    private Long reelsId;
    private String name;
    private String profileImagePath;
    private String type;
    private Long reelsOwnerId;

    @CreatedDate
    private LocalDateTime createdAt;
}

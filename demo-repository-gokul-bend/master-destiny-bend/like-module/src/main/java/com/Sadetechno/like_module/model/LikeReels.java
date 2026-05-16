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
@Document(collection = "reels_like")
public class LikeReels {

    @Id
    private String id;

    private Long reelsId;

    private Long userId;

    @CreatedDate
    private LocalDateTime notificationTime;
}

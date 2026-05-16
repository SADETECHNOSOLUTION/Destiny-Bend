package com.sadetech.media_aggregate_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentStatusDTO {
    private String id; // Use String for MongoDB ObjectId

    private Long statusId;
    private Long userId;

    private String textContent;

    @CreatedDate // Automatically store the creation date
    private LocalDateTime createdAt;
}

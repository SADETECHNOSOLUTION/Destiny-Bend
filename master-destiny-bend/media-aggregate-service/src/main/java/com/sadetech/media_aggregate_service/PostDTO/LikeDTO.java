package com.sadetech.media_aggregate_service.PostDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeDTO {
    private String id;
    private Long postId;
    private Long userId;

    @CreatedDate
    private LocalDateTime createdAt;
}

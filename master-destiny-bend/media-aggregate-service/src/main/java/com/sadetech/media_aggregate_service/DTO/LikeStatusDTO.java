package com.sadetech.media_aggregate_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeStatusDTO {
    private String id;

    private Long statusId;

    private Long userId;

    @CreatedDate
    private LocalDateTime notificationTime;
}

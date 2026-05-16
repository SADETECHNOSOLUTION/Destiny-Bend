package com.sadetech.media_aggregate_service.DTO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusDTO {
        private Long id;
        private String content;
        private String type;
        private LocalDateTime createdAt;
        private int duration;
        @Enumerated(EnumType.STRING)
        private Privacy privacy;
        private Long userId;
    }
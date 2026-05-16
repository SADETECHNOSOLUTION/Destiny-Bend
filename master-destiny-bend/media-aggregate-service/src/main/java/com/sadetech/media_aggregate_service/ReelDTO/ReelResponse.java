package com.sadetech.media_aggregate_service.ReelDTO;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReelResponse {
    private Long id;

    private String content; // Path to the reel file
    private int duration; // In seconds
    private String caption;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private Long userId; // ID of the user who uploaded the reel
    private String profileImagePath;
    private String name;

    @Enumerated(EnumType.STRING)
    private Privacy privacy;
}

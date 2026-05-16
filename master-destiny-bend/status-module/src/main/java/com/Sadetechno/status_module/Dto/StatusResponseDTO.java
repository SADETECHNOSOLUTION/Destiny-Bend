package com.Sadetechno.status_module.Dto;

import com.Sadetechno.status_module.model.Privacy;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusResponseDTO {

    private Long id;

    private String content;
    private String type;
    private LocalDateTime createdAt;
    private int duration;
    @Enumerated(EnumType.STRING)
    private Privacy privacy;

    private Long userId;
    private String profileImagePath;  // New field for profile image path
}

package com.sadetech.reels_module.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content; // Path to the reel file
    private int duration; // In seconds
    private String caption;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private Long userId; // ID of the user who uploaded the reel

    @Enumerated(EnumType.STRING)
    private Privacy privacy;
}


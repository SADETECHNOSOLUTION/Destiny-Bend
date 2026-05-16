package com.Sadetechno.status_module.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private String type;
    private LocalDateTime createdAt;
    private int duration;
    @Enumerated(EnumType.STRING)
    private Privacy privacy;
    private Long userId;
}



package com.Sadetechno.user_module.model;

import jakarta.persistence.*;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;



@Entity
@Table(name = "work_experience")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkExperience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String work;
    private String experience;
}


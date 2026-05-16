package com.Sadetechno.user_module.model;

import jakarta.persistence.*;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;



@Entity
@Table(name = "interests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Interest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String activity;
}



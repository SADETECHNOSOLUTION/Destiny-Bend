package com.sadetech.home_api_module.DTO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkExperience {

    private Long id;

    private String work;
    private String experience;
}


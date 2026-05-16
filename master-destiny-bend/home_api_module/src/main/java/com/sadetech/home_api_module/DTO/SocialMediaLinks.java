package com.sadetech.home_api_module.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SocialMediaLinks {

    private Long id;

    private String twitter;
    private String instagram;
    private String facebook;
    private String youtube;
    private String linkedin;
}


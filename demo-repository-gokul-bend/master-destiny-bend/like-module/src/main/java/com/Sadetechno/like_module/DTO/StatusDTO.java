package com.Sadetechno.like_module.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusDTO {

    private String profileImagePath;
    private String name;
    private Long statusId;
    private Long userId;
}

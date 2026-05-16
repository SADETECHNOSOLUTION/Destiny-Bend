package com.Sadetechno.comment_module.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReelsDTO {

    private String profileImagePath;
    private String name;
    private Long reelsId;
    private Long userId;
}

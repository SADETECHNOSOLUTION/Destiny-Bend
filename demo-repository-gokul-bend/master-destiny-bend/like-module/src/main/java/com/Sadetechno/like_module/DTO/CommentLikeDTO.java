package com.Sadetechno.like_module.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentLikeDTO {

    private String commentId;
    private Long userId;
    private String name;
}

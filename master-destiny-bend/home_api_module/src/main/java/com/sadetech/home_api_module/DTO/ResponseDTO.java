package com.sadetech.home_api_module.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {

    private Long postId;
    private String imageUrl;
    private String videoUrl;
    private PostType postType;

}

package com.sadetech.media_aggregate_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AggregateDTO {
    List<CommentStatusDTO> commentStatusDTO;
    List<LikeStatusDTO> likeStatusDTO;
    StatusDTO statusDTO;
    int likesCount;
    int commentCount;
}

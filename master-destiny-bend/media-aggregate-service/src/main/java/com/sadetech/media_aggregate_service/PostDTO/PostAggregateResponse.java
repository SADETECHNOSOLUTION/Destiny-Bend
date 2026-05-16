package com.sadetech.media_aggregate_service.PostDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostAggregateResponse {
    PostResponseDTO postResponseDTO;
    Map<String, Object> likeDTO;
    List<CommentResponse> commentResponses;
}

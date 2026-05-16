package com.sadetech.media_aggregate_service.ReelDTO;

import com.sadetech.media_aggregate_service.PostDTO.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllReelResponse {
    ReelResponse reelResponse;
    Map<String, Object> likeDTO;
    List<CommentReelsResponse> commentResponses;
}

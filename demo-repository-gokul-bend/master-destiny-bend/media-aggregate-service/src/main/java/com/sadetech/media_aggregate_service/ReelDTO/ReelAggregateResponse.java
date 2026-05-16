package com.sadetech.media_aggregate_service.ReelDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReelAggregateResponse {
    List<CommentReelsResponse> commentReelsResponse;
    ReelResponse reelResponse;
    Map<String ,Object> reelDTO;
}

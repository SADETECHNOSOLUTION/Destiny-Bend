package com.sadetech.media_aggregate_service.Controller;

import com.sadetech.media_aggregate_service.DTO.AggregateDTO;
import com.sadetech.media_aggregate_service.PostDTO.AllPostResponse;
import com.sadetech.media_aggregate_service.PostDTO.PostAggregateResponse;
import com.sadetech.media_aggregate_service.PostDTO.UserPostAggregateResponse;
import com.sadetech.media_aggregate_service.ReelDTO.AllReelResponse;
import com.sadetech.media_aggregate_service.ReelDTO.ReelAggregateResponse;
import com.sadetech.media_aggregate_service.Service.AggregateService;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/aggregate-media")
public class AggregateController {

    @Autowired
    private AggregateService aggregateService;

    private static final Logger logger = LoggerFactory.getLogger(AggregateController.class);

    @GetMapping("/status/{statusId}")
    public ResponseEntity<AggregateDTO> getStatusDetails(@PathVariable Long statusId){
        try {
            AggregateDTO aggregateDTO = aggregateService.getStatusDetails(statusId);
            return ResponseEntity.ok(aggregateDTO);

        } catch(FeignException e){
            e.printStackTrace();
            logger.warn("Error in connecting with feign client modules {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }catch (Exception e) {
            logger.warn("Got error in fetching status aggregate api {}",e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<PostAggregateResponse> getPostDetails(@PathVariable Long postId){
        try {
            PostAggregateResponse postAggregateResponse = aggregateService.getPostDetails(postId);
            return ResponseEntity.ok(postAggregateResponse);
        }catch(FeignException e){
             e.printStackTrace();
             logger.warn("Error in connecting with post feign client modules {}",e.getMessage());
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }catch(Exception e) {
            e.printStackTrace();
            logger.warn("Error fetching post details {}",e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/reel/{reelsId}")
    public ResponseEntity<ReelAggregateResponse> getReelsDetails(@PathVariable Long reelsId){
        try {
            ReelAggregateResponse reelAggregateResponse = aggregateService.getReelsDetails(reelsId);
            return ResponseEntity.ok(reelAggregateResponse);
        }catch(FeignException e){
            e.printStackTrace();
            logger.warn("Error in connecting with reel feign client modules {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }catch(Exception e) {
            e.printStackTrace();
            logger.warn("Error fetching reels details {}",e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/post/user/{userId}")
    public ResponseEntity<List<UserPostAggregateResponse>> getPostAggregatesByUserId(
           @PathVariable Long userId) {
        try{
            List<UserPostAggregateResponse> response = aggregateService.getPostDetailsByUserId(userId);
            return ResponseEntity.ok(response);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/status/user/{userId}")
    public ResponseEntity<List<AggregateDTO>> geStatusAggregatesByUserId(
            @PathVariable Long userId) {
        try{
            List<AggregateDTO> response = aggregateService.getStatusDetailsByUserId(userId);
            return ResponseEntity.ok(response);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/posts")
    public ResponseEntity<List<AllPostResponse>> getPostAggregates(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        try{
            List<AllPostResponse> response = aggregateService.getAllPostAggregates(page, size);
            return ResponseEntity.ok(response);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @GetMapping("/reels")
    public ResponseEntity<List<AllReelResponse>> getReelAggregates(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        List<AllReelResponse> reelResponses = aggregateService.getAllReel(page,size);
        return ResponseEntity.ok(reelResponses);
    }


}
package com.sadetech.reels_module.Controller;

import com.sadetech.reels_module.Dto.ReelResponseDTO;
import com.sadetech.reels_module.Model.Privacy;
import com.sadetech.reels_module.Model.Reel;
import com.sadetech.reels_module.Service.FileUploadService;
import com.sadetech.reels_module.Service.ReelService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reels")
public class ReelController {

    @Autowired
    private ReelService reelService;

    @PostMapping("/addReel")
    public ResponseEntity<Reel> postReel(
            @RequestParam(value = "file") MultipartFile file,
            @RequestParam(value = "caption",required = false) String caption,
            @RequestParam("duration") int duration,
            @RequestParam(value = "privacy" ,required = false,defaultValue = "PUBLIC") Privacy privacy,
            @RequestParam("userId") Long userId) throws IOException {

        Reel reel = reelService.saveReels(file, caption, duration, userId, privacy);
        return ResponseEntity.ok(reel);
    }

    @GetMapping("/getReel/{userId}")
    public ResponseEntity<List<ReelResponseDTO>> getReelsByUserId(@PathVariable Long userId) {
        List<ReelResponseDTO> reels = reelService.getReelsByUserId(userId);
        return ResponseEntity.ok(reels);
    }

    @GetMapping("/getAll/reel")
    public ResponseEntity<List<ReelResponseDTO>> getAllReels(){
        return ResponseEntity.ok(reelService.getAllReels());
    }

    @DeleteMapping("/deleteReel/{userId}/{id}")
    public ResponseEntity<String> deleteReel(@PathVariable Long userId, @PathVariable Long id) {
        try {
            String resultMessage = reelService.deleteReelByUserId(userId, id);
            return ResponseEntity
                    .status(HttpStatus.OK) // HTTP 200 OK
                    .body(resultMessage); // Message returned from service layer
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND) // HTTP 404 Not Found
                    .body(e.getMessage()); // Error message
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR) // HTTP 500 Internal Server Error
                    .body("Error deleting reel with ID " + id + " for user ID " + userId + ". Please try again later."); // General error message
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReelResponseDTO> getUserDetailsByReelsId(@PathVariable("id") Long id){
        ReelResponseDTO reel = reelService.getUserDetailsByReelsId(id);
        return ResponseEntity.status(HttpStatus.OK).body(reel);
    }


    @GetMapping("/reel/{id}/visibility/{userId}")
    public ResponseEntity<ReelResponseDTO> getReelByIdAndPrivacySetting(
            @PathVariable Long id,
            @PathVariable Long userId) {
        // Call the service method to get the post based on privacy settings
        ReelResponseDTO responseDTO = reelService.getReelsByPrivacy(id, userId);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/update-privacy/{id}")
    public ResponseEntity<Reel> updateReelPrivacy(@PathVariable Long id,@RequestParam Privacy privacy){
        try {
            Reel reel = reelService.updateReelPrivacy(id,privacy);
            return ResponseEntity.ok(reel);
        }catch (IllegalArgumentException e){
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    }
}

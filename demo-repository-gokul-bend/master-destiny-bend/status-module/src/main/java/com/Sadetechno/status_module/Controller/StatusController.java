package com.Sadetechno.status_module.Controller;
import com.Sadetechno.status_module.Dto.StatusResponseDTO;
import com.Sadetechno.status_module.Repository.StatusRepository;
import com.Sadetechno.status_module.Service.StatusService;
import com.Sadetechno.status_module.model.Privacy;
import com.Sadetechno.status_module.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/statuses")
public class StatusController {

    @Autowired
    private StatusService statusService;

    @Autowired
    private StatusRepository statusRepository;

    @PostMapping("/post")
    public ResponseEntity<Status> postStatus(
            @RequestParam(value = "file",required = false) MultipartFile file,
            @RequestParam(value = "type",required = false,defaultValue = "Image") String type,
            @RequestParam("duration") int duration,
            @RequestParam(value = "privacy",required = false,defaultValue = "PUBLIC") Privacy privacy,
            @RequestParam("userId") Long userId) throws IOException {

        Status status = statusService.saveStatus(file, type, duration, privacy, userId);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<StatusResponseDTO>> getStatusesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(statusService.getStatusesByUserId(userId));
    }
    @GetMapping("/user/status")
    public ResponseEntity<List<StatusResponseDTO>> getAllStatus(){
        return ResponseEntity.ok(statusService.getAllStatus());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Status>> getUserDetailsByStatusId(@PathVariable("id") Long id){
         Optional<Status> status = statusService.getUserDetailsByStatusId(id);
         return ResponseEntity.status(HttpStatus.OK).body(status);
    }

    @DeleteMapping("/delete/{userId}/{id}")
    public ResponseEntity<Void> deleteStatus(@PathVariable Long userId, @PathVariable Long id) {
        // Call the service method to delete the status
        statusService.deleteStatus(userId, id);
        // Return HTTP status OK when the deletion is successful
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/status/{id}/visibility/{userId}")
    public ResponseEntity<StatusResponseDTO> getStatusByIdAndPrivacySetting(
            @PathVariable Long id,
            @PathVariable Long userId) {
        // Call the service method to get the post based on privacy settings
        StatusResponseDTO statusResponseDTO = statusService.getStatusByPrivacy(id, userId);
        return ResponseEntity.ok(statusResponseDTO);
    }

    @PatchMapping("/update-privacy/{id}")
    public ResponseEntity<Status> updateStatusPrivacy(@PathVariable Long id, @RequestParam Privacy privacy){
        try {
            Status status = statusService.updateStatusPrivacy(id,privacy);
            return ResponseEntity.ok(status);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/uploads/{fileName:.+}")
    public ResponseEntity<Resource>serveFile(@PathVariable String fileName){
        try {
            Path filePath = Paths.get("static/uploads/").resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if(resource.exists()){
                String contentType = determineContentType(fileName);

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            }else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    private String determineContentType(String fileName) {
        if (fileName.toLowerCase().endsWith(".mp4")) {
            return "video/mp4";
        } else if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg") || fileName.toLowerCase().endsWith(".webp")) {
            return "image/jpeg";
        } else if (fileName.toLowerCase().endsWith(".png")) {
            return "image/png";
        } else {
            return "application/octet-stream";
        }
    }
}



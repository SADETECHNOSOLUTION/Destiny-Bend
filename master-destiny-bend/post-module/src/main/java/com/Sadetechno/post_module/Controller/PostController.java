package com.Sadetechno.post_module.Controller;

import com.Sadetechno.post_module.DTO.ResponseDTO;
import com.Sadetechno.post_module.Service.PostService;
import com.Sadetechno.post_module.model.Post;
import com.Sadetechno.post_module.model.PostVisibility;
import com.Sadetechno.post_module.model.PrivacySetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @PostMapping
    public ResponseEntity<Post> createPost(
            @RequestParam("userId") Long userId,
            @RequestParam("postType") String postType,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "videoFile", required = false) MultipartFile videoFile,
            @RequestParam(value = "privacySetting", required = false, defaultValue = "PUBLIC") String privacySetting,
            @RequestParam(value = "postVisibility",required = false,defaultValue = "PERSONAL")String postVisibility,
            @RequestParam(value = "parentName", required = false) String parentName,
            @RequestParam(value = "parentProfileImagePath", required = false) String parentProfileImagePath,
            @RequestParam(value = "parentDescription", required = false) String parentDescription,
            @RequestParam(value = "parentPostContent", required = false) String parentPostContent,
            @RequestParam(value = "parentPostCreatedAt", required = false) String parentPostCreatedAt
    )
    {

        try {
            Post post = postService.createPost(userId, postType, imageFile, videoFile, description, privacySetting,postVisibility,parentName,parentProfileImagePath,parentDescription,parentPostContent,parentPostCreatedAt);
            return new ResponseEntity<>(post, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.error("Got 400 error because of {}",e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            logger.error("Got 500 error because of {}",e.getMessage() );
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{postId}")
    public ResponseEntity<ResponseDTO> getPostWithUserDetails(@PathVariable Long postId) {
        ResponseDTO responseDto = postService.getPost(postId);  // This calls the method you already implemented

        if (responseDto != null) {
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) throws IOException {
        postService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/all")
    public List<ResponseDTO> getAllPosts() {
        return postService.getAllPost();
    }
    @GetMapping("/user/{userId}")
    public List<ResponseDTO> getPostsByUserId(@PathVariable Long userId) {
        return postService.getPostsByUserId(userId);
    }
    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("/uploads/{fileName:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                String contentType = determineContentType(fileName);

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private String determineContentType(String fileName) {
        if (fileName.toLowerCase().endsWith(".mp4")) {
            return "video/mp4";
        } else if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.toLowerCase().endsWith(".png")) {
            return "image/png";
        } else {
            return "application/octet-stream";
        }
    }

    @GetMapping("/user/{userId}/images")
    public ResponseEntity<List<ResponseDTO>> getImagePostsByUserId(@PathVariable Long userId) {
        List<ResponseDTO> posts = postService.getImagePostsByUserId(userId);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/videos")
    public ResponseEntity<List<ResponseDTO>> getVideoPostsByUserId(@PathVariable Long userId) {
        List<ResponseDTO> posts = postService.getVideoPostsByUserId(userId);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PatchMapping("/visibility/{userId}/{postVisibility}")
    public Post changeVisibilityType(@PathVariable Long userId, @PathVariable PostVisibility postVisibility){
        return postService.changeVisibilityType(userId,postVisibility);
    }

    @GetMapping("getPost-visibility/{userId}/{postVisibility}")
    public List<ResponseDTO> getPostByVisibility(@PathVariable Long userId, @PathVariable PostVisibility postVisibility){
        return postService.getPostByUserIdAndPostVisibility(userId,postVisibility);
    }

    @GetMapping("/posts/{postId}/visibility/{userId}")
    public ResponseEntity<ResponseDTO> getPostByIdAndPrivacySetting(
            @PathVariable Long postId,
            @PathVariable Long userId) {
            // Call the service method to get the post based on privacy settings
            ResponseDTO postResponse = postService.getPostByIdAndPrivacySetting(postId, userId);
            return ResponseEntity.ok(postResponse);
    }

    @PatchMapping("/update-privacy/{postId}")
    public ResponseEntity<Post> updatePrivacy(@PathVariable Long postId, @RequestParam PrivacySetting privacy){
        try {
            Post post = postService.updatePrivacy(postId,privacy);
            return ResponseEntity.ok(post);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
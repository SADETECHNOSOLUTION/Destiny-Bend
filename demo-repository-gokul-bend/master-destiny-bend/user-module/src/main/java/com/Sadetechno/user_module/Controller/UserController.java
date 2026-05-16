package com.Sadetechno.user_module.Controller;

import com.Sadetechno.user_module.DTO.BannerDTO;
import com.Sadetechno.user_module.DTO.ProfileDTO;
import com.Sadetechno.user_module.Service.UserService;
import com.Sadetechno.user_module.model.User;
import com.Sadetechno.user_module.model.UserCreationDTO;
import com.Sadetechno.user_module.model.Visibility;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public List<User> getAllUsers() {
        List<User> users = userService.getAllUsers();
        users.forEach(user -> {
            user.setProfileImagePath(user.getProfileImagePath());
            user.setBannerImagePath(user.getBannerImagePath());
        });
        return users;
    }

//    private String getFullUrl(String imagePath) {
//        if (imagePath == null || imagePath.isEmpty()) {
//            return null;
//        }
//        return "http://localhost:8080/api/users" + imagePath;
//    }

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
        } else if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.toLowerCase().endsWith(".png")) {
            return "image/png";
        } else {
            return "application/octet-stream";
        }
    }



    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/createUserWithImages")
    public ResponseEntity<?> createUserWithImages(@RequestParam("userJson") String userJson,
                                                  @RequestParam(value = "visibility",required = false, defaultValue = "PUBLIC")Visibility visibility,
                                                  @RequestParam("profileImage") MultipartFile profileImage,
                                                  @RequestParam("bannerImage") MultipartFile bannerImage) {
        try {
            UserCreationDTO userCreationDTO = new UserCreationDTO();
            userCreationDTO.setUserJson(userJson);  
            userCreationDTO.setProfileImage(profileImage);
            userCreationDTO.setBannerImage(bannerImage);
            userCreationDTO.setVisibility(visibility);
            User createdUser = userService.createUserWithImages(userCreationDTO);
            return ResponseEntity.status(HttpStatus.OK).body("User created successfully with ID: " + createdUser.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.getUserById(id).isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @ModelAttribute UserCreationDTO userUpdateDTO) {
        try {
            User updatedUser = userService.updateUser(id, userUpdateDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            logger.error("The error is {}",e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{userid}/updateContact")
    public ResponseEntity<?> updateEmailAndPhoneNumber(
            @PathVariable Long userid,
            @RequestParam String email,
            @RequestParam String phoneNumber) {

       try {
           Optional<User> user = userService.updateEmailAndPhoneNumber(userid, email, phoneNumber);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(user);
       }catch (IllegalArgumentException e){
           logger.warn("No id found {}",e.getMessage());
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating content");
       } catch (Exception e) {
           logger.warn("Bad request - {}",e.getMessage());
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
       }
    }


    @PatchMapping("/{id}/profile-image")
    public ResponseEntity<User> updateProfileImage(@PathVariable Long id, @Valid @ModelAttribute ProfileDTO profileDTO) {
        try {
            User updatedUser = userService.updateProfileImage(id, profileDTO);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}/banner-image")
    public ResponseEntity<User> updateBannerImage(@PathVariable Long id, @Valid @ModelAttribute BannerDTO bannerDTO){
        try {
            User updatedUser = userService.updateBannerImage(id,bannerDTO);
            return new ResponseEntity<>(updatedUser,HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Can't set profile image path {}",e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}/visibility")
    public ResponseEntity<?> updateUserVisibility(@PathVariable Long id, @RequestParam("visibility") Visibility visibility) {
        try {
            User updatedUser = userService.updateUserVisibility(id, visibility);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Can't set banner image path {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/getBy-email")
    public ResponseEntity<Optional<User>> getUserByEmail(@RequestParam String email) {
        Optional<User> user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/birthdays/today")
    public ResponseEntity<List<User>> getUsersWithTodayBirthday() {
        List<User> users = userService.getUsersWithTodayBirthday();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/birthdays/tomorrow")
    public ResponseEntity<List<User>> getUsersWithTomorrowBirthday() {
        return ResponseEntity.ok(userService.getUsersWithTomorrowBirthday());
    }

    @GetMapping("/birthdays/yesterday")
    public ResponseEntity<List<User>> getUsersWithYesterdayBirthday() {
        return ResponseEntity.ok(userService.getUsersWithYesterdayBirthday());
    }
}
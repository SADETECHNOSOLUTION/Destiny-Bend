package com.Sadetechno.user_module.model;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class UserCreationDTO {
    private String userJson; // Change this from User to String
    private MultipartFile profileImage;
    private MultipartFile bannerImage;
    private Visibility visibility;
}
package com.sadetech.settings.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IconFilesDTO {
    private MultipartFile homeIcon;
    private MultipartFile friendRequestIcon;
    private MultipartFile notificationIcon;
    private MultipartFile messageIcon;
    private MultipartFile widgetIcon;
    private MultipartFile settingsIcon;
    private MultipartFile searchIcon;
    private MultipartFile logoImage;

    // Getters and Setters
}

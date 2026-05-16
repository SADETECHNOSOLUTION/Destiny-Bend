package com.sadetech.settings.service;

import com.sadetech.settings.DTO.SystemSettingsDTO;
import com.sadetech.settings.model.SystemSettings;
import com.sadetech.settings.repository.SystemSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class SystemSettingsService {

    @Autowired
    private SystemSettingsRepository systemSettingsRepository;

    @Autowired
    private FileUploadService fileUploadService;

    public SystemSettings addSystemSettings(MultipartFile homeIcon,MultipartFile friendRequestIcon,MultipartFile notificationIcon,
                                            MultipartFile messageIcon, MultipartFile widgetIcon, MultipartFile settingsIcon, MultipartFile searchIcon,
                                            MultipartFile logoImage, SystemSettingsDTO settingsDTO) {
        try {
            // Upload all files
            SystemSettings systemSettings = new SystemSettings();
            systemSettings.setHomeIcon(fileUploadService.uploadFile(homeIcon));
            systemSettings.setFriendRequestIcon(fileUploadService.uploadFile(friendRequestIcon));
            systemSettings.setNotificationIcon(fileUploadService.uploadFile(notificationIcon));
            systemSettings.setMessageIcon(fileUploadService.uploadFile(messageIcon));
            systemSettings.setWidgetIcon(fileUploadService.uploadFile(widgetIcon));
            systemSettings.setSettingsIcon(fileUploadService.uploadFile(settingsIcon));
            systemSettings.setSearchIcon(fileUploadService.uploadFile(searchIcon));
            systemSettings.setLogoImage(fileUploadService.uploadFile(logoImage));

            // Populate non-file fields
            systemSettings.setColor(settingsDTO.getColor());
            systemSettings.setFooterContent(settingsDTO.getFooterContent());
            systemSettings.setCompanyName(settingsDTO.getCompanyName());
            systemSettings.setAddress(settingsDTO.getAddress());
            systemSettings.setPhoneNumber(settingsDTO.getPhoneNumber());
            systemSettings.setEmail(settingsDTO.getEmail());
            systemSettings.setSocialMediaLinks(settingsDTO.getSocialMediaLinks());
            systemSettings.setAboutUs(settingsDTO.getAboutUs());
            systemSettings.setPrivacyPolicy(settingsDTO.getPrivacyPolicy());
            systemSettings.setTermsAndConditions(settingsDTO.getTermsAndConditions());

            // Save and return
            return systemSettingsRepository.save(systemSettings);
        } catch (Exception e) {
            throw new RuntimeException("Error while adding system settings: " + e.getMessage(), e);
        }
    }

    public Optional<SystemSettings> getSystemSettings(int id) {
        return systemSettingsRepository.findById(id);
    }

    public SystemSettings updateSystemSettings(int id, MultipartFile homeIcon, MultipartFile friendRequestIcon,
                                               MultipartFile notificationIcon, MultipartFile messageIcon, MultipartFile widgetIcon,
                                               MultipartFile settingsIcon, MultipartFile searchIcon, MultipartFile logoImage,
                                               SystemSettingsDTO settingsDTO) {
        try {
            // Find the existing settings by ID
            Optional<SystemSettings> existingSettingsOpt = systemSettingsRepository.findById(id);
            if (existingSettingsOpt.isPresent()) {
                SystemSettings systemSettings = existingSettingsOpt.get();

                // Update the file fields if new files are provided, else keep the existing ones
                if (homeIcon != null && !homeIcon.isEmpty()) {
                    systemSettings.setHomeIcon(fileUploadService.uploadFile(homeIcon));
                }
                if (friendRequestIcon != null && !friendRequestIcon.isEmpty()) {
                    systemSettings.setFriendRequestIcon(fileUploadService.uploadFile(friendRequestIcon));
                }
                if (notificationIcon != null && !notificationIcon.isEmpty()) {
                    systemSettings.setNotificationIcon(fileUploadService.uploadFile(notificationIcon));
                }
                if (messageIcon != null && !messageIcon.isEmpty()) {
                    systemSettings.setMessageIcon(fileUploadService.uploadFile(messageIcon));
                }
                if (widgetIcon != null && !widgetIcon.isEmpty()) {
                    systemSettings.setWidgetIcon(fileUploadService.uploadFile(widgetIcon));
                }
                if (settingsIcon != null && !settingsIcon.isEmpty()) {
                    systemSettings.setSettingsIcon(fileUploadService.uploadFile(settingsIcon));
                }
                if (searchIcon != null && !searchIcon.isEmpty()) {
                    systemSettings.setSearchIcon(fileUploadService.uploadFile(searchIcon));
                }
                if (logoImage != null && !logoImage.isEmpty()) {
                    systemSettings.setLogoImage(fileUploadService.uploadFile(logoImage));
                }

                // Update non-file fields
                systemSettings.setId(id);
                systemSettings.setColor(settingsDTO.getColor());
                systemSettings.setFooterContent(settingsDTO.getFooterContent());
                systemSettings.setCompanyName(settingsDTO.getCompanyName());
                systemSettings.setAddress(settingsDTO.getAddress());
                systemSettings.setPhoneNumber(settingsDTO.getPhoneNumber());
                systemSettings.setEmail(settingsDTO.getEmail());
                systemSettings.setSocialMediaLinks(settingsDTO.getSocialMediaLinks());
                systemSettings.setAboutUs(settingsDTO.getAboutUs());
                systemSettings.setPrivacyPolicy(settingsDTO.getPrivacyPolicy());
                systemSettings.setTermsAndConditions(settingsDTO.getTermsAndConditions());

                // Save and return updated system settings
                return systemSettingsRepository.save(systemSettings); // This should now update the existing entity
            } else {
                throw new RuntimeException("SystemSettings with ID " + id + " not found.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while updating system settings: " + e.getMessage(), e);
        }
    }

}

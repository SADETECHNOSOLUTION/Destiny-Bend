package com.sadetech.settings.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadetech.settings.DTO.SystemSettingsDTO;
import com.sadetech.settings.model.*;
import com.sadetech.settings.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/settings")
public class SystemController {


    @Autowired
    private SystemSettingsService systemSettingsService;

    @PostMapping("/add")
    public ResponseEntity<SystemSettings> addSystemSettings(
            @RequestParam MultipartFile homeIcon,
            @RequestParam MultipartFile friendRequestIcon,
            @RequestParam MultipartFile notificationIcon,
            @RequestParam MultipartFile messageIcon,
            @RequestParam MultipartFile widgetIcon,
            @RequestParam MultipartFile settingsIcon,
            @RequestParam MultipartFile searchIcon,
            @RequestParam MultipartFile logoImage,
            @RequestParam String settingsDTO) {

        // Manually parse the settingsDTO string to a SystemSettingsDTO object
        ObjectMapper objectMapper = new ObjectMapper();
        SystemSettingsDTO dto;
        try {
            dto = objectMapper.readValue(settingsDTO, SystemSettingsDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing settingsDTO", e);
        }

        SystemSettings settings = systemSettingsService.addSystemSettings(
                homeIcon, friendRequestIcon, notificationIcon, messageIcon,
                widgetIcon, settingsIcon, searchIcon, logoImage, dto);

        return ResponseEntity.ok(settings);
    }

    @GetMapping("/get-setting/{id}")
    public ResponseEntity<?> getSystemSettings(@PathVariable int id){
        try {
            Optional<SystemSettings> settings = systemSettingsService.getSystemSettings(id);
            return ResponseEntity.status(HttpStatus.OK).body(settings);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Settings not found for the id");
        }
    }

    @PutMapping("/update/{id}")
    public SystemSettings updateSystemSettings(
            @PathVariable int id,
            @RequestParam(value = "homeIcon", required = false) MultipartFile homeIcon,
            @RequestParam(value = "friendRequestIcon", required = false) MultipartFile friendRequestIcon,
            @RequestParam(value = "notificationIcon", required = false) MultipartFile notificationIcon,
            @RequestParam(value = "messageIcon", required = false) MultipartFile messageIcon,
            @RequestParam(value = "widgetIcon", required = false) MultipartFile widgetIcon,
            @RequestParam(value = "settingsIcon", required = false) MultipartFile settingsIcon,
            @RequestParam(value = "searchIcon", required = false) MultipartFile searchIcon,
            @RequestParam(value = "logoImage", required = false) MultipartFile logoImage,
            @RequestParam(required = false) String settingsDTO) {

        ObjectMapper objectMapper = new ObjectMapper();
        SystemSettingsDTO dto;
        try {
            dto = objectMapper.readValue(settingsDTO, SystemSettingsDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing settingsDTO", e);
        }

        return systemSettingsService.updateSystemSettings(
                id,homeIcon, friendRequestIcon, notificationIcon, messageIcon,
                widgetIcon, settingsIcon, searchIcon, logoImage, dto);


    }

    }